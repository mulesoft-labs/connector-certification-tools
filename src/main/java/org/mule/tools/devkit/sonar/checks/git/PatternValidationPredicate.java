package org.mule.tools.devkit.sonar.checks.git;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.mule.tools.devkit.sonar.exception.SonarCheckException;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PatternValidationPredicate implements Predicate<String> {

    private final File file;

    public PatternValidationPredicate(File file) {
        this.file = file;
    }

    @Override
    public boolean apply(@Nullable String pattern) {
        try {
            return Iterables.any(Lists.newArrayList(new Scanner(file)), Predicates.containsPattern(pattern));
        } catch (FileNotFoundException e) {
            throw new SonarCheckException(e);
        }
    }
}
