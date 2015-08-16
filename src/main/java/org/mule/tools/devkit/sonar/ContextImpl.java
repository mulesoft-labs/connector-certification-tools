package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ContextImpl implements Context {

    private static final String DEVKIT_VERSION_XPATH = "/pom:project/pom:parent/pom:version/text()";
    private static final String CONNECTOR_CATEGORY_XPATH = "/pom:project/pom:properties/pom:category/text()";

    private final String devkitVersion;
    private final String category;
    private final ConnectorModelIml model;
    private ClassLoader classLoader;
    private final static ThreadLocal<Context> threadLocal = new ThreadLocal<>();
    private final static Map<Path, Context> instancesByPath = new ConcurrentHashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    public ContextImpl(@NonNull final Path basePath) {

        // Init class loader ...
        try {
            this.classLoader = new ModuleClassLoader(basePath);
        } catch (IOException | XPathExpressionException | SAXException e) {
            throw new IllegalStateException(e);
        }

        // Init pom properties ...
        this.devkitVersion = (String) XmlUtils.evalXPathOnPom(basePath, DEVKIT_VERSION_XPATH, XPathConstants.STRING);
        logger.debug("Parsed devkit version -> {}", devkitVersion);

        this.category = (String) XmlUtils.evalXPathOnPom(basePath, CONNECTOR_CATEGORY_XPATH, XPathConstants.STRING);
        logger.debug("Parsed Category version -> {}", category);

        try {
            final Optional<Path> connectorPath = Files.walk(basePath.resolve("src/main/java"), FileVisitOption.FOLLOW_LINKS)
                    .filter(path -> path.toString().endsWith("Connector.java")).findAny();
            this.model = new ConnectorModelIml(connectorPath.get());
        } catch (IOException e) {
            throw new DevKitSonarRuntimeException(e);
        }
    }

    @Override public @NonNull String getDevKitVersion() {
        return this.devkitVersion;
    }

    @NonNull @Override public ClassLoader getModuleClassLoader() {
        return classLoader;
    }

    @Override public @NonNull ConnectorModel getConnectorModel() {
        return model;
    }

    @NonNull public static Context getInstance(@NonNull Path basePath) {
        Context context = instancesByPath.get(basePath);
        if (context == null) {
            context = new ContextImpl(basePath);
            instancesByPath.put(basePath, context);
        }
        return context;
    }

    public void setup() {
        threadLocal.set(this);
    }

    @NonNull public static Context getInstance() {
        return threadLocal.get();
    }

}

