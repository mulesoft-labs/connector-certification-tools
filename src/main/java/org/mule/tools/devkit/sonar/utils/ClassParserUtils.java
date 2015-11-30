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
import org.sonar.plugins.java.api.tree.*;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.*;

public class ClassParserUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassParserUtils.class);

    private static final Set<String> primitives = new HashSet<>();

    public static final ThreadLocal<ProjectClasspath> PROJECT_CLASSPATH_THREAD_LOCAL = new ThreadLocal<>();

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

    private static final Set<String> defaultImportedClasses = new HashSet<>();

    static {
        defaultImportedClasses.add("Integer");
        defaultImportedClasses.add("Double");
        defaultImportedClasses.add("Long");
        defaultImportedClasses.add("Float");
        defaultImportedClasses.add("Character");
        defaultImportedClasses.add("Byte");
        defaultImportedClasses.add("Short");
        defaultImportedClasses.add("Boolean");
        defaultImportedClasses.add("Object");
        defaultImportedClasses.add("String");
    }

    private static final Map<String, Class<?>> primitiveToBoxedType = new HashMap<>();

    static {
        primitiveToBoxedType.put("int", java.lang.Integer.class);
        primitiveToBoxedType.put("double", java.lang.Double.class);
        primitiveToBoxedType.put("long", java.lang.Long.class);
        primitiveToBoxedType.put("float", java.lang.Float.class);
        primitiveToBoxedType.put("char", java.lang.Character.class);
        primitiveToBoxedType.put("byte", java.lang.Byte.class);
        primitiveToBoxedType.put("short", java.lang.Short.class);
        primitiveToBoxedType.put("boolean", java.lang.Boolean.class);
    }

    private static final ImmutableSet<Class<?>> allowedComplexTypes;

    static {
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        builder.add(Integer.class);
        builder.add(Double.class);
        builder.add(Long.class);
        builder.add(Float.class);
        builder.add(Character.class);
        builder.add(Byte.class);
        builder.add(Short.class);
        builder.add(Boolean.class);
        builder.add(BigDecimal.class);
        builder.add(String.class);
        builder.add(Enum.class);
        builder.add(Date.class);
        allowedComplexTypes = builder.build();
    }

    private ClassParserUtils() {
    }

    public static Predicate<VariableTree> getComplexTypePredicate(final Set<ImportTree> imports) {
        return new Predicate<VariableTree>() {

            @Override
            public boolean apply(@Nullable VariableTree input) {
                return input != null && input.type().is(Tree.Kind.IDENTIFIER) && !isSimpleType(input.type(), imports);
            }
        };
    }

    public static boolean isPrimitive(@NonNull final Tree type, @NonNull Set<ImportTree> imports) {
        Optional<Class<?>> clazz = classForName(type, imports);
        return clazz.isPresent() && primitiveToBoxedType.values().contains(clazz.get());
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

    public static boolean is(@NonNull AnnotationTree annotation, @NonNull final Class<?> annotationClass) {
        final String annotationSimpleName = annotation.annotationType().toString();
        return annotationSimpleName.equals(annotationClass.getSimpleName()) || annotationSimpleName.equals(annotationClass.getCanonicalName());
    }

    @Nonnull
    public static Optional<Class<?>> classForName(@NonNull final String classNameDef, @NonNull final Set<ImportTree> imports) {

        // Is a generic declaration ?. Remove generic type ..
        final String className = classNameDef.split("<")[0];

        // Is the class name fully qualified ?
        Optional<Class<?>> result = Optional.absent();
        final boolean isFullQualified = className.contains(".");
        if (isFullQualified) {
            result = findClass(className);
        }

        // Object is a primitive class type ?
        if (!result.isPresent()) {
            if (primitives.contains(className)) {
                result = Optional.<Class<?>>fromNullable(primitiveToBoxedType.get(className));
            } else if (defaultImportedClasses.contains(className)) {
                result = findClass("java.lang." + className);
            }
        }

        // Type to be resolved based on the imports ...
        if (!result.isPresent()) {
            final Predicate<ImportTree> predicate = new Predicate<ImportTree>() {

                @Override
                public boolean apply(@Nullable ImportTree input) {
                    return input != null && !input.isStatic() && extractImport(input.qualifiedIdentifier()).endsWith("." + className);
                }
            };
            if (Iterables.any(imports, predicate)) {
                final ImportTree importTree = Iterables.find(imports, predicate);
                final String qualifiedName = extractImport(importTree.qualifiedIdentifier());
                result = findClass(qualifiedName);
            }
        }

        if (!result.isPresent()) {
            result = Optional.<Class<?>>fromNullable(
                    Iterables.getFirst(Iterables.transform(Iterables.filter(Iterables.transform(Iterables.transform(Iterables.filter(imports, new Predicate<ImportTree>() {

                        @Override
                        public boolean apply(@Nullable ImportTree input) {
                            return input != null && !input.isStatic() && extractImport(input.qualifiedIdentifier()).endsWith(".*");
                        }
                    }), new Function<ImportTree, String>() {

                        @Override
                        public String apply(@Nullable ImportTree input) {
                            if (input == null) {
                                return null;
                            }
                            final String statementStr = input.qualifiedIdentifier().toString();
                            return statementStr.substring(0, statementStr.length() - 1) + className;
                        }
                    }), new Function<String, Optional<Class<?>>>() {

                        @Override
                        @NonNull
                        public Optional<Class<?>> apply(@Nullable String input) {
                            return findClass(input);
                        }
                    }), new Predicate<Optional<Class<?>>>() {

                        @Override
                        public boolean apply(@Nullable Optional<Class<?>> input) {
                            return input != null && input.isPresent();
                        }

                    }), new Function<Optional<Class<?>>, Class<?>>() {

                        @Override
                        public Class<?> apply(@Nullable Optional<Class<?>> input) {
                            if (input == null) {
                                return null;
                            }
                            return input.get();
                        }
                    }), null));
        }

        if (!result.isPresent()) {
            logger.warn("Class name can not be loaded '{}'", className);
        }
        return result;
    }

    @NonNull
    private static Optional<Class<?>> findClass(@NonNull String className) {
        Optional<Class<?>> result = Optional.absent();
        try {
            ProjectClasspath projectClasspath = PROJECT_CLASSPATH_THREAD_LOCAL.get();
            result = Optional.<Class<?>>fromNullable(Class.forName(className, true, projectClasspath != null ? projectClasspath.getClassloader() : ClassParserUtils.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
            logger.debug("Couldn't find class {}", className);
            // Ignore ...
        }
        return result;
    }

    @Nonnull
    public static Optional<Class<?>> classForName(@NonNull final Tree type, @NonNull final Set<ImportTree> imports) {
        String classNameDef = extractType(type);
        return classForName(classNameDef, imports);
    }

    @Nonnull
    private static String extractType(@NonNull Tree type) {
        String classNameDef = type.toString();
        if (type.is(Tree.Kind.VARIABLE)) {
            classNameDef = ((VariableTree) type).type().toString();
        }

        return classNameDef;
    }

    @Nonnull
    private static String extractImport(Tree tree) {
        List<String> list = Lists.newArrayList();
        while (tree.is(Tree.Kind.MEMBER_SELECT)) {
            final MemberSelectExpressionTree memberSelectExpressionTree = (MemberSelectExpressionTree) tree;
            list.add(memberSelectExpressionTree.identifier().toString());
            tree = memberSelectExpressionTree.expression();
        }
        list.add(tree.toString());
        return Joiner.on(".").join(Lists.reverse(list));
    }
}
