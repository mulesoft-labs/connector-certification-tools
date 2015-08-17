package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.tools.devkit.sonar.ClassParserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetadataCategoriesVerifier extends SourceTreeVerifier {

    final List<String> metadataClassesName = new ArrayList<>();

    @Override @NonNull final public Object visitClass(@NonNull ClassTree classTree, @NonNull Trees trees) {
        final Object result = super.visitClass(classTree, trees);

        final Optional<? extends AnnotationTree> metadataCategory = ClassParserUtils.find(classTree.getModifiers().getAnnotations(), MetaDataCategory.class);
        if (metadataCategory.isPresent()) {
            metadataClassesName.add(metadataCategory.get().toString());
        }

        // @Todo: Complete !!!!!.
        return result;
    }

    @Override @NonNull final public Object visitMethod(@NonNull final MethodTree methodTree, Trees trees) {
        final Optional<? extends AnnotationTree> metadataCategory = ClassParserUtils.find(methodTree.getModifiers().getAnnotations(), MetaDataCategory.class);
        if (metadataCategory.isPresent()) {
            metadataClassesName.add(metadataCategory.get().toString());
        }
        return super.visitMethod(methodTree, trees);
    }

}
