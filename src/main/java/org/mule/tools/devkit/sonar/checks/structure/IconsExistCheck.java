package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@Rule(key = IconsExistCheck.KEY, name = "Icons should be present", description = "There should exist icons for your connector. Normally there will be an icon for the connector, maybe another icon for endpoint, and perhaps another one for transformers. Each one needs to be in two sizes: 24x16 and 48x32 pixels.", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class IconsExistCheck implements StructureCheck {

    public static final String KEY = "icons-exist";

    public static final String ICONS_FOLDER = "icons";
    private static final Predicate<File> PNG_EXTENSION_PREDICATE = new Predicate<File>() {

        @Override
        public boolean apply(@Nullable File input) {
            return input.getName().endsWith(".png");
        }
    };

    private final FileSystem fileSystem;

    public IconsExistCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        Path path = fileSystem.baseDir().toPath().resolve(ICONS_FOLDER);
        if (!Files.exists(path)) {
            issues.add(new ConnectorIssue(KEY, String.format("Folder '%s' is missing.", path.toFile().getName())));
        } else {
            final Collection<File> files = FileUtils.listFiles(path.toFile(), TrueFileFilter.TRUE, null);
            if (!Iterables.any(files, PNG_EXTENSION_PREDICATE)) {
                issues.add(new ConnectorIssue(KEY, String.format("Folder '%s' has no png files.", path.toFile().getName())));
            } else {
                for (File file : Iterables.filter(files, PNG_EXTENSION_PREDICATE)) {
                    try {
                        if (file.getName().endsWith("24x16.png")) {
                            checkIconSize(issues, file, 24, 16);
                        } else if (file.getName().endsWith("48x32.png")) {
                            checkIconSize(issues, file, 48, 32);
                        } else {
                            issues.add(new ConnectorIssue(KEY, String.format("Unexpected file found in 'icons' folder: '%s'.", file.getName())));
                        }
                    } catch (IOException e) {
                        issues.add(new ConnectorIssue(KEY, String.format("Problem reading icon file '%s'.", file.getName())));
                    }
                }
            }
        }
        return issues;
    }

    private void checkIconSize(List<ConnectorIssue> issues, File file, int expectedWidth, int expectedHeight) throws IOException {
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();
        if (width != expectedWidth && height != expectedHeight) {
            issues.add(new ConnectorIssue(KEY, String.format("Icon file '%s' found but image size is incorrect (%dx%d).", file.getName(), width, height)));
        }
    }

}
