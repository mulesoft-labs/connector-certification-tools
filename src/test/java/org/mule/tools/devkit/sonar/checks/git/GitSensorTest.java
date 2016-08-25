package org.mule.tools.devkit.sonar.checks.git;

import org.junit.Test;
import org.reflections.Reflections;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Project;
import org.sonar.check.Rule;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class GitSensorTest {

    @Test
    public void analyzeTest() {
        GitSensor sensor = new GitSensor();
        SensorContext sensorContext = mock(SensorContext.class);
        FileSystem fileSystem = mock(FileSystem.class);
        when(fileSystem.inputFiles(any())).thenReturn(new ArrayList<>());
        FilePredicates filePredicates = mock(FilePredicates.class);
        when(filePredicates.matchesPathPattern(anyString())).thenReturn(mock(FilePredicate.class));
        when(fileSystem.predicates()).thenReturn(filePredicates);
        when(sensorContext.fileSystem()).thenReturn(fileSystem);
        sensor.analyse(mock(Project.class), sensorContext);
        verify(fileSystem, times(new Reflections(getClass().getPackage().getName()).getTypesAnnotatedWith(Rule.class).size())).inputFiles(any());
    }
}
