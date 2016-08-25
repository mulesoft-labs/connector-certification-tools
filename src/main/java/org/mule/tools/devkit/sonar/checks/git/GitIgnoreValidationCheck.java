package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.util.List;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

public abstract class GitIgnoreValidationCheck implements GitCheck {

    private final String key;
    private final String messageTemplate;
    private final List<String> patterns;

    public GitIgnoreValidationCheck(String key, String messageTemplate, String... patterns) {
        this.key = key;
        this.messageTemplate = messageTemplate;
        this.patterns = newArrayList(patterns);
    }

    @Override
    public void analyse(Project project, SensorContext context, InputFile inputFile) {
        for (String pattern : transform(filter(patterns, not(new PatternValidationPredicate(inputFile.file()))), new RemoveRegexFunction())) {
            NewIssue issue = context.newIssue()
                    .forRule(RuleKey.of("git", key));
            issue.at(issue.newLocation()
                    .message(String.format(messageTemplate, pattern))
                    .on(inputFile));
            issue.save();
        }
    }
}
