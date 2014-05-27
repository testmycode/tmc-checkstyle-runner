package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

public class TMCConfigurationBuilderTest {

    @Test
    public void shouldReturnNullOnUnexistentConfiguration() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/valid/trivial"));

        assertNull(config);
    }

    @Test
    public void shouldReturnTMCConfiguration() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/valid/trivial_with_configuration"));

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldNotFailOnAdditionalJSONData() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration"));

        assertNotNull(config);
        assertEquals("default-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldNotFailOnAdditionalJSONRootData() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/invalid/maven_exercise_with_configuration"));

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }
}
