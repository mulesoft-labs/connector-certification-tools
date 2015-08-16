package org.mule.tools.devkit.sonar;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassParserUtils {

    final private static Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    private static final Set<String> primitives = new HashSet<>();
    private static final String REF_ONLY_CLASS = "RefOnly";
    private static final String DEFAULT_PAYLOADCLASS = "Default(\"#[payload]\")";
    private static final String PROCESSOR_CLASSNAME = "Processor";
    private static final String OPTIONAL_CLASSNAME = "Optional";
    private static final String DEFAULT_CLASSNAME = "Default";
    private static final String CONNECTOR_CLASSNAME = "Connector";

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

    private ClassParserUtils() {

    }

    public static boolean isPrimitive(@NonNull final Tree type) {
        return primitives.contains(type.toString()) || primitivesClasses.contains(type.toString());
    }

    public static boolean isEnum(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        final Optional<Class<?>> optional = classForName(type, imports);
        final boolean result = optional.isPresent() && optional.get().isEnum();
        return result;
    }

    private static boolean containsAnnotation(final @NonNull VariableTree variable, final @NonNull Predicate<AnnotationTree> predicate) {
        return variable.getModifiers().getAnnotations().stream().anyMatch(predicate);
    }

    public static boolean isMarkedAsDefault(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassParserUtils::isDefaultAnnotation);
    }

    public static boolean isMarkedAsPayloadDefault(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassParserUtils::isDefaultPayloadAnnotation);
    }

    public static boolean isMarkedAsOptional(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassParserUtils::isOptionalAnnotation);
    }

    public static boolean isMarkedAsRefOnly(final @NonNull VariableTree variable) {
        return containsAnnotation(variable, ClassParserUtils::isRefOnlyAnnotation);
    }

    public static boolean isDefaultAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, DEFAULT_CLASSNAME);
    }

    public static boolean isDefaultPayloadAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, DEFAULT_PAYLOADCLASS);
    }

    public static boolean isProcessorAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, PROCESSOR_CLASSNAME);
    }

    public static boolean isOptionalAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, OPTIONAL_CLASSNAME);
    }

    public static boolean isConnectorAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, CONNECTOR_CLASSNAME);
    }

    private static boolean isDevKitAnnotation(@NonNull AnnotationTree annotation, @NonNull final String classsName) {
        return annotation.toString().startsWith("@" + classsName) || annotation.toString().startsWith("@org.mule.api.annotations." + classsName);
    }

    public static boolean isRefOnlyAnnotation(@NonNull final AnnotationTree annotation) {
        return isDevKitAnnotation(annotation, REF_ONLY_CLASS);
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
                final Optional<ImportTree> classImport = imports.stream().filter(imp -> !imp.isStatic() && imp.getQualifiedIdentifier().toString().endsWith("." + className))
                        .findFirst();

                if (classImport.isPresent()) {
                    final String qualifiedName = classImport.get().getQualifiedIdentifier().toString();
                    final Context instance = Context.getInstance();
                    final ClassLoader classLoader = instance.getModuleClassLoader();

                    final Class<?> value = Class.forName(qualifiedName, false, classLoader);
                    result = Optional.ofNullable(value);
                }
            }

            // @Todo: Type to resolved based on the based on wildcard ...
//            if (!result.isPresent()) {
//                final Set<ImportTree> importsWithWildcard = imports.stream().filter(imp -> !imp.isStatic() && imp.getQualifiedIdentifier().toString().endsWith(".*"))
//                        .collect(Collectors.toSet());
//
//                final Stream<@NonNull String> fullQualifiedClasses = importsWithWildcard.stream().map(imp -> imp.toString().substring(0, imp.toString().length() - 1) + className);
//
//                final Context instance = Context.getInstance();
//                final ClassLoader classLoader = instance.getModuleClassLoader();
//
//                fullQualifiedClasses.filter(fullClassName -> {Class.forName(fullClassName, false, classLoader)})
//            }

        } catch (ClassNotFoundException e) {
            // Ignore ..
        }

        logger.debug("Class name -> {} , Resolved Class: {}", className, result.toString());
        return result;
    }

    public static Optional<Class<?>> classForName(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        return classForName(type.toString(), imports);
    }

}
