package org.mule.tools.devkit.sonar.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.rule.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonRulesLoader {

    final static Map<String, RuleBuilder> builders = new HashMap<>();

    static {
        builders.put("source.xml", XmlRule::new);
        builders.put("source.pom", PomRule::new);
        builders.put("source.java", JavaSourceRule::new);
        builders.put("structure", DirectoryStructureRule::new);
    }

    @NonNull public static Set<Rule> build() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream jsonStream = JsonRulesLoader.class.getClassLoader().getResourceAsStream("rules.json");
        final JsonRules rulesDef = mapper.readValue(jsonStream, JsonRules.class);

        return rulesDef.getRules().stream().map(JsonRulesLoader::defToRule).collect(Collectors.toSet());
    }

    @NonNull private static Rule defToRule(@NonNull final JsonRule ruleDef) {
        final String type = ruleDef.getType();

        final RuleBuilder ruleBuilder = builders.get(type);
        if (ruleBuilder == null) {
            throw new IllegalStateException("Unsupported type:" + type);
        }

        final Rule.Documentation documentation = DocumentationImpl.create(ruleDef.getId(), ruleDef.getBrief(), ruleDef.getDescription(), null);
        return ruleBuilder.create(documentation, ruleDef.getAcceptRegexp(), ruleDef.getAssert());

    }

    @FunctionalInterface interface RuleBuilder {

        @NonNull Rule create(Rule.Documentation documentation, @NonNull final String verifyExpression, @NonNull String assertExp);
    }

}
