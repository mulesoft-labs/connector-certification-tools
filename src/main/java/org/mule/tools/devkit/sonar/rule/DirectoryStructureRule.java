package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class DirectoryStructureRule extends AbstractRule {

    private final Pattern verifyExpression;

    public DirectoryStructureRule(@NonNull final Documentation documentation, @NonNull String acceptRegexp, @NonNull final String verifyExpression) {
        super(documentation, acceptRegexp);
        this.verifyExpression = Pattern.compile(verifyExpression);
    }

    @Override
    public boolean verify(@NonNull Path rootPath, @NonNull Path childPath) throws DevKitSonarRuntimeException {
        return verifyExpression.matcher(childPath.toString()).find();
    }
}
