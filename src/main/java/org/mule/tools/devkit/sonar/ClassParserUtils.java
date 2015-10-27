package org.mule.tools.devkit.sonar;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree;
import org.apache.commons.lang3.ClassUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.RefOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ClassParserUtils {

    final private static Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    private static final Set<String> primitives = new HashSet<>();
    private static final String DEFAULT_PAYLOAD_EXPRESSION = "Default(\"#[payload]\")";

    static {
        primitives.add("int");
        primitives.add("double");
        primitives.add("long");
        primitives.add("float");
        primitives.add("char");
        primitives.add("byte");
        primitives.add("short");
        primitives.add("boolean");
    }

    final private static Set<String> defaultImportedClasses = new HashSet<>();

    static {
        defaultImportedClasses.add("Integer");
        defaultImportedClasses.add("String");
        defaultImportedClasses.add("Long");
        defaultImportedClasses.add("Float");
        defaultImportedClasses.add("Double");
        defaultImportedClasses.add("Character");
        defaultImportedClasses.add("Byte");
        defaultImportedClasses.add("Boolean");
        defaultImportedClasses.add("Object");
    }

    final private static Map<String, Class<?>> primitiveToBoxedType = new HashMap<>();

    static {
        primitiveToBoxedType.put("int", java.lang.Integer.class);
        primitiveToBoxedType.put("double", java.lang.Double.class);
        primitiveToBoxedType.put("boolean", java.lang.Boolean.class);
        primitiveToBoxedType.put("float", java.lang.Float.class);
        primitiveToBoxedType.put("long", java.lang.Long.class);
        primitiveToBoxedType.put("char", java.lang.Character.class);
        primitiveToBoxedType.put("byte", java.lang.Byte.class);
        primitiveToBoxedType.put("short", java.lang.Short.class);
    }

    final private static Set<Class<?>> allowedComplexTypes = new HashSet<>();

    static {
        allowedComplexTypes.add(java.lang.String.class);
        allowedComplexTypes.add(java.math.BigDecimal.class);
    }

    private ClassParserUtils() {

    }

    public static boolean isPrimitive(@NonNull final Tree type, @NonNull Set<ImportTree> imports) {
        Optional<Class<?>> clazz = classForName(type, imports);
        return clazz.isPresent() && ClassUtils.isPrimitiveWrapper(clazz.get());
    }

    public static boolean isSimpleType(@NonNull final Tree type, @NonNull Set<ImportTree> imports) {
        Optional<Class<?>> clazz = classForName(type, imports);
        boolean result = isPrimitive(type, imports) || isEnum(type, imports) || (clazz.isPresent() && allowedComplexTypes.contains(clazz.get()));
        logger.debug("Type '{}' is a simple type -> '{}'", type.toString(), result);
        return result;
    }

    public static boolean isEnum(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        final Optional<Class<?>> optional = classForName(type, imports);
        return optional.isPresent() && optional.get().isEnum();
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
        return is(annotation, Default.class);
    }

    public static boolean isDefaultPayloadAnnotation(@NonNull final AnnotationTree annotation) {
        return annotation.toString().startsWith("@" + DEFAULT_PAYLOAD_EXPRESSION)
                || annotation.toString().startsWith("@org.mule.api.annotations.param." + DEFAULT_PAYLOAD_EXPRESSION);
    }

    public static boolean isProcessorAnnotation(@NonNull final AnnotationTree annotation) {
        return is(annotation, Processor.class);
    }

    public static boolean isOptionalAnnotation(@NonNull final AnnotationTree annotation) {
        return is(annotation, org.mule.api.annotations.param.Optional.class);
    }

    public static boolean isConnectorAnnotation(@NonNull final AnnotationTree annotation) {
        return is(annotation, Connector.class);
    }

    public static boolean isRefOnlyAnnotation(@NonNull final AnnotationTree annotation) {
        return is(annotation, RefOnly.class);
    }

    public static boolean is(@NonNull AnnotationTree annotation, @NonNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.toString().split(Pattern.quote("("))[0];
        return annotationSimpleName.equals("@" + annotationClass.getSimpleName()) || annotationSimpleName.equals("@" + annotationClass.getCanonicalName());
    }

    public static Optional<Class<?>> classForName(@NonNull final String classNameDef, @NonNull final Set<ImportTree> imports) {

        // Is a generic declaration ?. Remove generic type ..
        String className = classNameDef.split("<")[0];

        // Is the class name fully qualified ?
        Optional<Class<?>> result = Optional.empty();
        final boolean isFullQualified = className.contains(".");
        if (isFullQualified) {
            result = findClass(className);
        }

        // Object is a primitive class type ?
        if (!result.isPresent()) {
            if (primitives.contains(className)) {
                result = Optional.of(primitiveToBoxedType.get(className));
            } else if (defaultImportedClasses.contains(className)) {
                result = findClass("java.lang." + className);
            }
        }

        // Type to resolved based on the imports ...
        if (!result.isPresent()) {
            final Optional<ImportTree> classImport = imports.stream().filter(imp -> !imp.isStatic() && imp.getQualifiedIdentifier().toString().endsWith("." + className))
                    .findFirst();

            if (classImport.isPresent()) {
                final String qualifiedName = classImport.get().getQualifiedIdentifier().toString();
                result = findClass(qualifiedName);
            }
        }

        if (!result.isPresent()) {
            final Stream<ImportTree> statementsWithWildcard = imports.stream().filter(imp -> !imp.isStatic() && imp.getQualifiedIdentifier().toString().endsWith(".*"));
            final Stream<String> importsWithWildcard = statementsWithWildcard.map(imp -> {
                final String statementStr = imp.getQualifiedIdentifier().toString();
                return statementStr.substring(0, statementStr.length() - 1);
            });

            final Stream<@NonNull String> fullQualifiedClasses = importsWithWildcard.map(importStr -> importStr + className);
            result = fullQualifiedClasses.map(ClassParserUtils::findClass).filter(Optional::isPresent).map(Optional::get).findAny();
        }

        if (!result.isPresent()) {
            logger.warn("Class name can not be loaded '{}'", className);
        }
        return result;
    }

    @NonNull
    private static Optional<Class<?>> findClass(@NonNull String className) {
        Optional<Class<?>> result = Optional.empty();

        try {
            final Context instance = Context.getInstance();
            final ClassLoader classLoader = instance.getModuleClassLoader();

            result = Optional.ofNullable(Class.forName(className, false, classLoader));
        } catch (ClassNotFoundException e) {
            // Ignore ...
        }
        return result;
    }

    public static Optional<Class<?>> classForName(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        String classNameDef = extractType(type);
        return classForName(classNameDef, imports);
    }

    private static String extractType(@NonNull Tree type) {
        String classNameDef = type.toString();
        if (type.getKind() == Tree.Kind.VARIABLE) {
            classNameDef = ((JCTree.JCVariableDecl) type).getType().toString();
        }

        return classNameDef;
    }

    public static boolean contains(@NonNull final List<? extends AnnotationTree> annotations, @NonNull final Class<?> annotationClass) {
        return annotations.stream().anyMatch(a -> ClassParserUtils.is(a, annotationClass));
    }

    public static Optional<? extends AnnotationTree> find(@NonNull final List<? extends AnnotationTree> annotations, @NonNull final Class<?> annotationClass) {
        return annotations.stream().filter(a -> ClassParserUtils.is(a, annotationClass)).findFirst();
    }

}
