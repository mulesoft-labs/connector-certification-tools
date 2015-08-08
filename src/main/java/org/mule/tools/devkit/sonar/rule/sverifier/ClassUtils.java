package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

class ClassUtils {

    final private static Set<String> primitives = new HashSet<>();
    private static final String REF_ONLY_CLASS = "@RefOnly";
    private static final String OPTIONAL_CLASS = "@Optional";
    private static final String DEFAULT_CLASS = "@Default";
    private static final String DEFAULT_PAYLOAD_CLASS = "@Default(\"#[payload]\")";

    static {
        primitives.add("int");
        primitives.add("double");
        primitives.add("long");
        primitives.add("float");
        primitives.add("char");
        primitives.add("byte");
        primitives.add("boolean");
    }

    final private static Set<String> primitivesClasses = new HashSet<>();

    static {
        primitivesClasses.add("Integer");
        primitivesClasses.add("String");
        primitivesClasses.add("Long");
        primitivesClasses.add("Float");
        primitivesClasses.add("Double");
        primitivesClasses.add("Character");
        primitivesClasses.add("Byte");
        primitivesClasses.add("Boolean");
    }

    private ClassUtils() {

    }

    public static boolean isPrimitive(@NonNull final Tree type) {
        return primitives.contains(type.toString()) || primitivesClasses.contains(type.toString());
    }

    private static boolean containsAnnotation(final @NonNull VariableTree variable, final @NonNull Predicate<AnnotationTree> predicate) {
        return variable.getModifiers().getAnnotations().stream().anyMatch(predicate);
    }

    public static boolean isMarkedAsDefault(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassUtils::isDefaultAnnotation);
    }

    public static boolean isMarkedAsPayloadDefault(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassUtils::isDefaultPayloadAnnotation);
    }

    public static boolean isMarkedAsOptional(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassUtils::isOptionalAnnotation);
    }

    public static boolean isMarkedAsRefOnly(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassUtils::isRefOnlyAnnotation);
    }

    public static boolean isDefaultAnnotation(@NonNull final AnnotationTree annotation) {
        return annotation.toString().startsWith(DEFAULT_CLASS);
    }

    public static boolean isDefaultPayloadAnnotation(@NonNull final AnnotationTree annotation) {
        return annotation.toString().startsWith(DEFAULT_PAYLOAD_CLASS);
    }

    public static boolean isOptionalAnnotation(@NonNull final AnnotationTree annotation) {

        return annotation.toString().startsWith(OPTIONAL_CLASS);

    }

    public static boolean isRefOnlyAnnotation(@NonNull final AnnotationTree annotation) {
        return annotation.toString().startsWith(REF_ONLY_CLASS);
    }

}
