package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public class ContextImpl implements Context {

    private final Path basePath;

    public ContextImpl(@NonNull final Path basePath) {
        this.basePath = basePath;

    }

    void init()  {

        try {
            final ProjectClassLoader projectClassloader = new ProjectClassLoader(basePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    @NonNull public static Context getInstance(final @NonNull Path basePath) {
        return new ContextImpl(basePath);
    }

    @Override public @NonNull ConnectorModel getConnectorModel() {
        return null;
    }

}
