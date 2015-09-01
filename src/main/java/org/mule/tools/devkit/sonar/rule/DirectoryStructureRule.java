package org.mule.tools.devkit.sonar.rule;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.regex.qual.Regex;
import org.mule.tools.devkit.sonar.ClassProperty;
import org.mule.tools.devkit.sonar.Context;
import org.mule.tools.devkit.sonar.ValidationError;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DirectoryStructureRule extends AbstractRule {

    @Regex public static final String VELOCITY_VARIABLE = "\\$\\{([^${}]+)}";
    final private static Logger logger = LoggerFactory.getLogger(DirectoryStructureRule.class);

    private Template template;
    private final Set<ClassProperty> templateProperties;

    public DirectoryStructureRule(@NonNull final Documentation documentation, @NonNull String accept, @NonNull final String assertExp) {
        super(documentation, "pom.xml$");
        this.templateProperties = new LinkedHashSet<>();

        try {
            this.initTemplate(assertExp);
        } catch (ParseException e) {
            throw new DevKitSonarRuntimeException(e);
        }
    }

    private void initTemplate(@NonNull final String verifyExpression) throws ParseException {

        // Init pattern ...
        final RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();

        final StringReader reader = new StringReader(verifyExpression);
        SimpleNode node = runtimeServices.parse(reader, this.getDocumentation().getId());

        final Template template = new Template();
        template.setRuntimeServices(runtimeServices);
        template.setData(node);
        template.initDocument();
        this.template = template;

        // Parse expression looking for variable ...
        final Pattern pattern = Pattern.compile(VELOCITY_VARIABLE);
        final Matcher matcher = pattern.matcher(verifyExpression);

        while (matcher.find()) {
            final String key = matcher.group(1);
            final ClassProperty property = ClassProperty.to(key);
            this.templateProperties.add(property);
        }

    }

    @Override public Set<ValidationError> verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException {

        final Set<VelocityContext> contexts = this.buildContexts(basePath);
        final List<String> msgs = new ArrayList<>();
        for (VelocityContext context : contexts) {
            final StringWriter sw = new StringWriter();
            template.merge(context, sw);

            // Does the file exist?
            final String child = sw.toString();
            if (!Files.exists(basePath.resolve(child))) {
                msgs.add("File '" + child + "' does not exist.");
            }
        }
        return buildError(msgs);
    }

    @NonNull public Set<VelocityContext> buildContexts(@NonNull final Path basePath) {

        // Find defined variables ...
        final Context context = Context.getInstance(basePath);
        final List<List<String>> varValues = templateProperties.stream().map(var -> context.getConnectorModel().getProperty(var)).collect(Collectors.toList());

        final Set<VelocityContext> result = new HashSet<>();
        if (!templateProperties.isEmpty()) {
            final List<List<String>> varValuesPerm = permute(varValues, 0);

            // Create contexts ...
            for (final List<String> values : varValuesPerm) {
                final VelocityContext vcontext = new VelocityContext();
                logger.debug("Value to process {} {}", templateProperties, values);

                // Populate values ...
                int i = 0;
                for (final ClassProperty property : templateProperties) {
                    vcontext.put(property.toKey(), values.get(i));
                    i++;
                }
                result.add(vcontext);
            }
        } else {
            // If not variable is part of the expression, the expression must be evaluated ...
            result.add(new VelocityContext());
        }

        logger.debug("Velocity context:" + result);
        return result;
    }

    @NonNull static public List<List<String>> permute(@NonNull final List<List<String>> lists, int level) {

        List<List<String>> result = new ArrayList<>();
        if (level == lists.size() - 1) {
            result = lists.get(level).stream().map(Collections::singletonList).collect(Collectors.toList());
        } else {

            List<List<String>> children = new ArrayList<>();
            if (level < lists.size()) {
                children = DirectoryStructureRule.permute(lists, level + 1);
            }

            final List<String> parents = lists.get(level);
            for (String parent : parents) {
                for (List<String> child : children) {
                    final List<String> item = new ArrayList<>();
                    item.add(parent);
                    item.addAll(child);
                    result.add(item);
                }
            }
        }

        return result;
    }

}
