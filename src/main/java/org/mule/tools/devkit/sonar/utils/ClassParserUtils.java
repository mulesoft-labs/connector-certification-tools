package org.mule.tools.devkit.sonar.utils;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.ParameterizedTypeTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.plugins.java.api.tree.TypeTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.plugins.java.api.tree.WildcardTree;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

public class ClassParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    private static final ImmutableSet<String> allowedComplexTypes = ImmutableSet.<String> builder()
            .add(Integer.class.getName())
            .add(Double.class.getName())
            .add(Long.class.getName())
            .add(Float.class.getName())
            .add(Character.class.getName())
            .add(Byte.class.getName())
            .add(Short.class.getName())
            .add(Boolean.class.getName())
            .add(BigDecimal.class.getName())
            .add(String.class.getName())
            .add(Enum.class.getName())
            .add(Date.class.getName())
            .add("org.mule.api.MuleMessage")
            .build();

    private static final ImmutableSet<String> parameterizableTypes = ImmutableSet.<String> builder().add(List.class.getName()).add(Map.class.getName()).build();

    private static final ImmutableSet<String> connectionConfigTypes = ImmutableSet.<String> builder()
            .add("Configuration")
            .add("ConnectionManagement")
            .add("OAuth")
            .add("OAuth2")
            .build();

    public static final Predicate<AnnotationTree> ANNOTATION_TREE_PREDICATE = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && input.annotationType().is(Kind.IDENTIFIER);
        }
    };

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

    public static Predicate<AnnotationTree> hasAnnotationPredicate(final String annotationClassName) {
        return new Predicate<AnnotationTree>() {

            @Override
            public boolean apply(@Nullable AnnotationTree input) {
                return input != null && is(input, annotationClassName);
            }
        };
    }

    public static Predicate<AnnotationTree> hasConfigAnnotationPredicate() {

        return new Predicate<AnnotationTree>() {

            @Override
            public boolean apply(@Nullable AnnotationTree input) {
                if (input == null) {
                    return false;
                }
                String name;
                if (input.annotationType().is(Kind.MEMBER_SELECT)) {
                    name = ((MemberSelectExpressionTree) input.annotationType()).identifier().name();
                } else {
                    name = ((IdentifierTree) input.annotationType()).name();
                }
                return connectionConfigTypes.contains(name);
            }
        };
    }

    public static boolean isSimpleType(@NotNull final TypeTree type) {
        final Type symbolType = type.symbolType();
        boolean result;
        if (symbolType.isPrimitive() || symbolType.symbol().isEnum()) {
            result = true;
        } else if (type.is(Tree.Kind.PARAMETERIZED_TYPE)) {
            ParameterizedTypeTree parametrizedType = (ParameterizedTypeTree) type;
            result = parameterizableTypes.contains(parametrizedType.type().symbolType().fullyQualifiedName())
                    && Iterables.all(parametrizedType.typeArguments(), simpleTypeCategoryPredicate());
        } else {
            result = symbolType.isClass() && allowedComplexTypes.contains(symbolType.fullyQualifiedName());
        }
        logger.debug("Type '{}' is a simple type -> '{}'", type.toString(), result);
        return result;
    }

    public static boolean is(@NotNull AnnotationTree annotation, @NotNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClass.getSimpleName()) || annotationSimpleName.equals(annotationClass.getCanonicalName());
    }

    public static boolean is(@NotNull AnnotationTree annotation, @NotNull final String annotationClassName) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClassName) || annotationSimpleName.equals(annotationClassName.substring(annotationClassName.lastIndexOf(".") + 1));
    }

    public static String getStringForType(TypeTree type) {
        StringBuilder sb = new StringBuilder();
        if (type.is(Tree.Kind.PARAMETERIZED_TYPE)) {
            ParameterizedTypeTree parametrizedType = (ParameterizedTypeTree) type;
            sb.append(parametrizedType.type().toString());
            sb.append("<");
            sb.append(Joiner.on(", ").join(Iterables.transform(parametrizedType.typeArguments(), stringCategoryFunction())));
            sb.append(">");
        } else {
            sb.append(type.toString());
        }
        return sb.toString();
    }

    private static Function<Tree, String> stringCategoryFunction() {
        return new Function<Tree, String>() {

            @Override
            public String apply(@Nullable Tree input) {
                if (input == null) {
                    return "[null]";
                } else if (input.is(Tree.Kind.IDENTIFIER)) {
                    return getStringForType((TypeTree) input);
                } else if (input.is(Tree.Kind.EXTENDS_WILDCARD)) {
                    return "? extends " + getStringForType(((WildcardTree) input).bound());
                } else if (input.is(Tree.Kind.UNBOUNDED_WILDCARD)) {
                    return "?";
                }
                return "UNKNOWN";
            }
        };
    }

    private static Predicate<Tree> simpleTypeCategoryPredicate() {
        return new Predicate<Tree>() {

            @Override
            public boolean apply(@Nullable Tree input) {
                if (input == null) {
                    return false;
                } else if (input.is(Tree.Kind.IDENTIFIER)) {
                    return isSimpleType((TypeTree) input);
                } else if (input.is(Tree.Kind.EXTENDS_WILDCARD)) {
                    return isSimpleType(((WildcardTree) input).bound());
                } else if (input.is(Tree.Kind.UNBOUNDED_WILDCARD)) {
                    return false;
                }
                return false;
            }
        };
    }

    public static String extractFullyQualifiedClassName(MemberSelectExpressionTree tree) {
        final Deque<String> stack = new ArrayDeque<>();
        ExpressionTree expressionTree = tree;
        while (expressionTree.is(Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree mset = (MemberSelectExpressionTree) expressionTree;
            stack.push(mset.identifier().name());
            expressionTree = mset.expression();
        }
        stack.push(((IdentifierTree) expressionTree).name());
        return Joiner.on('.').join(stack);
    }

    public static boolean isTestClass(ClassTree classTree) {
        return Iterables.any(classTree.members(), new Predicate<Tree>() {

            @Override
            public boolean apply(@Nullable Tree input) {
                return input != null && input.is(Kind.METHOD) && Iterables.any(((MethodTree) input).modifiers().annotations(), hasAnnotationPredicate("org.junit.Test"));
            }
        });
    }
}
