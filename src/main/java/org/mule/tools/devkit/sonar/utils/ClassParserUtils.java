package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.ProjectClasspath;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ImportTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TypeTree;
import org.sonar.plugins.java.api.tree.VariableTree;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    private static final ImmutableSet<String> allowedComplexTypes;

    static {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        builder.add(Integer.class.getName());
        builder.add(Double.class.getName());
        builder.add(Long.class.getName());
        builder.add(Float.class.getName());
        builder.add(Character.class.getName());
        builder.add(Byte.class.getName());
        builder.add(Short.class.getName());
        builder.add(Boolean.class.getName());
        builder.add(BigDecimal.class.getName());
        builder.add(String.class.getName());
        builder.add(Enum.class.getName());
        builder.add(Date.class.getName());
        allowedComplexTypes = builder.build();
    }

    private ClassParserUtils() {
    }

    public static Predicate<VariableTree> complexTypePredicate() {
        return new Predicate<VariableTree>() {

            @Override
            public boolean apply(@Nullable VariableTree input) {
                return input != null && !isSimpleType(input.type());
            }
        };
    }

    public static Predicate<VariableTree> simpleTypePredicate() {
        return new Predicate<VariableTree>() {

            @Override
            public boolean apply(@Nullable VariableTree input) {
                return input != null && isSimpleType(input.type());
            }
        };
    }

    public static Predicate<AnnotationTree> hasAnnotationPredicate(final Class<?> annotationClass) {
        return new Predicate<AnnotationTree>() {

            @Override
            public boolean apply(@Nullable AnnotationTree input) {
                return input != null && is(input, annotationClass);
            }
        };
    }

    public static boolean isSimpleType(@NonNull final TypeTree type) {
        final Type symbolType = type.symbolType();
        boolean result = symbolType.isPrimitive() || symbolType.symbol().isEnum() || (symbolType.isClass() && allowedComplexTypes.contains(symbolType.fullyQualifiedName()));
        logger.debug("Type '{}' is a simple type -> '{}'", type.toString(), result);
        return result;
    }

    public static boolean is(@NonNull AnnotationTree annotation, @NonNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClass.getSimpleName()) || annotationSimpleName.equals(annotationClass.getCanonicalName());
    }

}
