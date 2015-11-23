package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import javax.annotation.Nullable;
import java.math.BigDecimal;

public class ClassParserUtils {

    final private static Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    final private static ImmutableSet<String> allowedComplexTypes;

    static {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        builder.add(Integer.class.getSimpleName());
        builder.add(Double.class.getSimpleName());
        builder.add(Boolean.class.getSimpleName());
        builder.add(Float.class.getSimpleName());
        builder.add(Long.class.getSimpleName());
        builder.add(Character.class.getSimpleName());
        builder.add(Byte.class.getSimpleName());
        builder.add(Short.class.getSimpleName());
        builder.add(BigDecimal.class.getSimpleName());
        builder.add(String.class.getSimpleName());
        builder.add(Enum.class.getSimpleName());
        allowedComplexTypes = builder.build();
    }

    private ClassParserUtils() {
    }

    public static final Predicate<VariableTree> COMPLEX_TYPE_PREDICATE = new Predicate<VariableTree>() {

        @Override
        public boolean apply(@Nullable VariableTree input) {
            return input != null && input.type().is(Tree.Kind.IDENTIFIER) && !isSimpleType((IdentifierTree) input.type());
        }
    };

    public static boolean isSimpleType(@NotNull final IdentifierTree type) {

        // TODO We should find a better way to implement this. It's a bit naive of an implementation. Could get idea's from Paulo's code
//        boolean result = symbolType.isPrimitive() || symbolType.symbol().isEnum() || (symbolType.isClass() && allowedComplexTypes.contains(symbolType.fullyQualifiedName()));
        boolean result = allowedComplexTypes.contains(type.name());
        logger.debug("Type '{}' is a simple type -> '{}'", type.toString(), result);
        return result;
    }

    public static boolean is(@NotNull AnnotationTree annotation, @NotNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClass.getSimpleName()) || annotationSimpleName.equals(annotationClass.getCanonicalName());
    }

}
