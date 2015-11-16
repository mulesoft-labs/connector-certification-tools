package org.mule.tools.devkit.sonar.checks;

import org.mule.tools.devkit.sonar.utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Initializer;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;

import javax.xml.xpath.XPathConstants;

public class ProjectContextInitializer extends Initializer {

    private static final Logger logger = LoggerFactory.getLogger(LicenseByCategoryCheck.class);

    private static final String CONNECTOR_CATEGORY_XPATH = "/pom:project/pom:properties/pom:category/text()";

    private final Settings settings;
    private final FileSystem fs;

    public ProjectContextInitializer(Settings settings, FileSystem fs) {
        this.settings = settings;
        this.fs = fs;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void execute(Project project) {
        String category = (String) XmlUtils.evalXPathOnPom(fs.baseDir().toPath(), CONNECTOR_CATEGORY_XPATH, XPathConstants.STRING);
        settings.setProperty("category", category);
        logger.info("Found category in pom.xml: " + category);
    }

}
