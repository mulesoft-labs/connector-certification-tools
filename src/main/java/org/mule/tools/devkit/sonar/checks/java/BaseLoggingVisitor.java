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
    public final void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @SuppressWarnings("deprecation")
    protected final void logAndRaiseIssue(@NotNull Tree classTree, String message) {
        LoggerFactory.getLogger(getClass()).info(message);
        // TODO need to change this to new form context.reportIssue()
        context.addIssue(classTree, this, message);
    }

}
