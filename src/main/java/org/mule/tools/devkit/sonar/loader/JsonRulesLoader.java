package org.mule.tools.devkit.sonar.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.rule.PomRule;
import org.mule.tools.devkit.sonar.rule.XmlRule;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonRulesLoader {

    final static Map<String, RuleBuilder> builders = new HashMap<>();
    static {
        builders.put("xml", XmlRule::new);
        builders.put("pom", PomRule::new);
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
        if(ruleBuilder==null){
            throw new IllegalStateException("Unsupported type:" + type);
        }

        return ruleBuilder.create(ruleDef.getId(),ruleDef.getBrief(),ruleDef.getDescription(),ruleDef.getXPath(),ruleDef.getAcceptRegexp());

    }

    @FunctionalInterface interface RuleBuilder {

        @NonNull
        Rule create(@NonNull final String id, @NonNull final String brief, @NonNull final String description, @NonNull final String xpathExp, @NonNull String acceptRegexp);
    }

}
