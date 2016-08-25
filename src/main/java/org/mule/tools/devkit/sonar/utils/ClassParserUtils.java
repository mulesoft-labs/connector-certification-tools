package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    public static final String FQN_DEFAULT = "org.mule.api.annotations.param.Default";
    public static final String FQN_OPTIONAL = "org.mule.api.annotations.param.Optional";
    public static final String FQN_REFONLY = "org.mule.api.annotations.param.RefOnly";

    private static final Set<String> allowedComplexTypes = ImmutableSet.<String> builder()
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
            .add(Calendar.class.getName())
            .add("org.mule.api.MuleMessage")
            .add("org.mule.api.MuleEvent")
            .add("javax.xml.stream.XMLStreamReader")
            .build();

    private static final Set<String> parameterizableTypes = ImmutableSet.<String> builder().add(List.class.getName()).add(Map.class.getName()).build();

    private static final Set<String> connectionConfigTypes = ImmutableSet.<String> builder().add("Configuration").add("ConnectionManagement").add("OAuth").add("OAuth2").build();

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

    public static Predicate<AnnotationTree> hasSimpleAnnotationPredicate(final String annotationName) {
        return new Predicate<AnnotationTree>() {

            @Override
            public boolean apply(@Nullable AnnotationTree input) {
                return input != null && isSimpleName(input, annotationName);
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
        final String annotationSimpleName = getNameForSimpleAndFQN(annotation);
        return annotationSimpleName.equals(annotationClassName) || annotationSimpleName.equals(annotationClassName.substring(annotationClassName.lastIndexOf(".") + 1));
    }

    public static boolean isSimpleName(@NotNull AnnotationTree annotation, @NotNull final String annotationName) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equalsIgnoreCase(annotationName);
    }

    public static String getNameForSimpleAndFQN(@NotNull AnnotationTree annotation) {
        // handle FQN annotations
        return annotation.annotationType().is(Tree.Kind.MEMBER_SELECT) ?
                ((MemberSelectExpressionTree)annotation.annotationType()).identifier().name() : annotation.annotationType().toString();
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

    public static boolean isTestClass(ClassTree classTree) {
        return Iterables.any(classTree.members(), new Predicate<Tree>() {

            @Override
            public boolean apply(@Nullable Tree input) {
                return input != null && input.is(Kind.METHOD) && Iterables.any(((MethodTree) input).modifiers().annotations(), hasAnnotationPredicate("org.junit.Test"));
            }
        });
    }

    public static String concatenate(@Nullable ExpressionTree tree) {
        if (tree == null) {
            return "";
        }
        Deque<String> pieces = new LinkedList<>();
        ExpressionTree expr = tree;
        while (expr.is(Tree.Kind.MEMBER_SELECT)) {
            MemberSelectExpressionTree mse = (MemberSelectExpressionTree) expr;
            pieces.push(mse.identifier().name());
            pieces.push(".");
            expr = mse.expression();
        }
        if (expr.is(Tree.Kind.IDENTIFIER)) {
            IdentifierTree idt = (IdentifierTree) expr;
            pieces.push(idt.name());
        }

        StringBuilder sb = new StringBuilder();
        for (String piece : pieces) {
            sb.append(piece);
        }
        return sb.toString();
    }
}
