package org.mule.tools.devkit.sonar.rule;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;
import org.mule.tools.devkit.sonar.XmlUtils;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import javax.xml.xpath.XPathConstants;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public class PomRule extends AbstractRule {

    private final String acceptXPath;
    private final String assertExp;

    public PomRule(final Rule.Documentation documentation, @NonNull String accept, @Nullable final String assertExp) {
        super(documentation, "pom.xml$");
        this.acceptXPath = accept;
        this.assertExp = assertExp;
    }

    @Override
    public boolean accepts(@NonNull final Path basePath, @NonNull final Path childPath) {
        boolean result = super.accepts(basePath, childPath);
        if (!StringUtils.isBlank(acceptXPath) && result) {
            result = (boolean) XmlUtils.evalXPathOnPom(basePath, acceptXPath, XPathConstants.BOOLEAN);
        }
        return result;
    }

    @Override
    public @NonNull Set<ValidationError> verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException {
        final boolean result = (boolean) XmlUtils.evalXPathOnPom(basePath, assertExp, XPathConstants.BOOLEAN);
        return result ? Collections.emptySet() : Collections.singleton(ValidationError.create(this.getDocumentation(), "Expressions could not be satisfied '" + assertExp + "'."));
    }
}
