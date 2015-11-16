package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TypeTree;
import org.sonar.plugins.java.api.tree.VariableTree;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ClassParserUtils {

    final private static Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    final private static ImmutableSet<String> allowedComplexTypes;

    static {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        builder.add(Integer.class.getName());
        builder.add(Double.class.getName());
        builder.add(Boolean.class.getName());
        builder.add(Float.class.getName());
        builder.add(Long.class.getName());
        builder.add(Character.class.getName());
        builder.add(Byte.class.getName());
        builder.add(Short.class.getName());
        builder.add(BigDecimal.class.getName());
        builder.add(String.class.getName());
        allowedComplexTypes = builder.build();
    }

    private ClassParserUtils() {
    }

    public static final Predicate<VariableTree> COMPLEX_TYPE_PREDICATE = new Predicate<VariableTree>() {

        @Override
        public boolean apply(@Nullable VariableTree variableTree) {
            return !isSimpleType(variableTree.type());
        }
    };

    public static boolean isSimpleType(@NotNull final TypeTree type) {
        final Type symbolType = type.symbolType();
        boolean result = symbolType.isPrimitive() || symbolType.symbol().isEnum() || (symbolType.isClass() && allowedComplexTypes.contains(symbolType.fullyQualifiedName()));
        logger.debug("Type '{}' is a simple type -> '{}'", type.toString(), result);
        return result;
    }

    public static boolean is(@NotNull AnnotationTree annotation, @NotNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClass.getSimpleName()) || annotationSimpleName.equals(annotationClass.getCanonicalName());
    }

}
