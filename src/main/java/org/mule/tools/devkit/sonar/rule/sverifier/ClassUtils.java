package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

class ClassUtils {

    final private static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static final Set<String> primitives = new HashSet<>();
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

    public static Optional<Class<?>> classForName(@NonNull final String className, @NonNull final Set<ImportTree> imports) {

        Optional<Class<?>> result = Optional.empty();
        try {
            // Is the class name fully qualified ?
            final boolean isFullQualified = className.contains(".");
            if (isFullQualified) {
                result = Optional.ofNullable(Class.forName(className));
            }

            // Object is a primitive type  ?
            if (!result.isPresent()) {
                if (primitivesClasses.contains(className)) {
                    result = Optional.ofNullable(Class.forName("java.lang." + className));
                }
            }

            // Type to resolved based on the imports ...
            if (!result.isPresent()) {
                final Optional<ImportTree> classImp = imports.stream().filter(imp -> !imp.isStatic() && imp.getQualifiedIdentifier().toString().endsWith("." + className))
                        .findFirst();

                if (classImp.isPresent()) {
                    final String qualifiedName = classImp.get().getQualifiedIdentifier().toString();
                    final Class<?> value = Class.forName(qualifiedName);
                    result = Optional.ofNullable(value);

                }
            }

        } catch (ClassNotFoundException e) {
            // Ignore ..
        }

        logger.info("Class name -> {} , Resolved Class: {}", className, result.toString());
        return result;
    }

    public static Optional<Class<?>> classForName(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        return classForName(type.toString(), imports);
    }

}
