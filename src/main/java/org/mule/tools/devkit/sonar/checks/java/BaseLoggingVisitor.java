package org.mule.tools.devkit.sonar.checks.java;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.Tree;

public abstract class BaseLoggingVisitor extends BaseTreeVisitor implements JavaFileScanner {

    JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    protected final void logAndRaiseIssue(@NotNull Tree tree, String message) {
        LoggerFactory.getLogger(getClass()).info(message);
        context.reportIssue(this, tree, message);
    }

}
