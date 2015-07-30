package org.mule.tools.devkit.sonar.test;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.nio.file.Paths;

interface TestData {

    @NonNull
    static Path rootPath() {
        return Paths.get("src/test/resources/simple_connector").toAbsolutePath();
    }
}
