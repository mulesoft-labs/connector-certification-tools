package org.mule.tools.devkit.sonar.rule;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryStructureRule extends AbstractRule {

    private Template template;

    public DirectoryStructureRule(@NonNull final Documentation documentation, @NonNull String acceptRegexp, @NonNull final String verifyExpression) {
        super(documentation, acceptRegexp);
        try {
            this.initTemplate(verifyExpression);
        } catch (ParseException e) {
            throw new DevKitSonarRuntimeException(e);
        }
    }

    private void initTemplate(@NonNull String verifyExpression) throws ParseException {

        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();

        final StringReader reader = new StringReader(verifyExpression);
        SimpleNode node = runtimeServices.parse(reader, this.getDocumentation().getId());

        final Template template = new Template();
        template.setRuntimeServices(runtimeServices);
        template.setData(node);
        template.initDocument();
        this.template = template;
    }

    @Override
    public boolean verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException {
        final VelocityContext context = new VelocityContext();

        // @todo: Hook with devkit documentation metadata ....
        context.put("PROCESSOR", "World");

        final StringWriter sw = new StringWriter();
        template.merge(context, sw);
        final Path fullPath = basePath.resolve(sw.toString());

        return Files.exists(fullPath);
    }
}
