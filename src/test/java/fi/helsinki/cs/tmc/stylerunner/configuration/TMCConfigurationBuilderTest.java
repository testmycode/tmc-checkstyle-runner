package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import static org.junit.Assert.*;

public class TMCConfigurationBuilderTest {

    @Test
    public void shouldReturnDefaultTMCConfigurationOnNonexistentConfiguration() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/valid/trivial"));

        assertNotNull(config);
    }

    @Test
    public void shouldReturnTMCConfiguration() throws CheckstyleException, FileNotFoundException {

        final File projectDirectory = new File("test-projects/valid/trivial_with_configuration");
        final TMCConfiguration config = TMCConfigurationBuilder.build(projectDirectory);

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
        assertNotNull(config.getInputSource(projectDirectory));
    }

    @Test
    public void shouldNotFailOnAdditionalAndInvalidJSONProperties() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration"));

        assertNotNull(config);
        assertEquals("default-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldNotFailOnAdditionalJSONRootProperties() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/invalid/maven_exercise_with_configuration"));

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }
}
