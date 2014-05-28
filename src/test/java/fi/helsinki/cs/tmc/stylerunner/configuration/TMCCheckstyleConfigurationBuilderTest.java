package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class TMCCheckstyleConfigurationBuilderTest {

    @Rule
    public ExpectedException publicThrown = ExpectedException.none();

    @Test
    public void shouldReturnDefaultTMCConfigurationOnNonexistentConfiguration() throws CheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/valid/trivial"));

        assertNotNull(config);
    }

    @Test
    public void shouldReturnTMCConfiguration() throws CheckstyleException, FileNotFoundException {

        final File projectDirectory = new File("test-projects/valid/trivial_with_configuration");
        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(projectDirectory);

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
        assertNotNull(config.getInputSource(projectDirectory));
    }

    @Test
    public void shouldNotFailOnAdditionalAndInvalidJSONProperties() throws CheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration"));

        assertNotNull(config);
        assertEquals("default-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldNotFailOnAdditionalJSONRootProperties() throws CheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/maven_exercise_with_configuration"));

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldReturnDefaultTMCConfigurationIfConfigurationIsInvalid() throws CheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/maven_with_configuration"));

        assertEquals("default-checkstyle.xml", config.getRule());
        assertTrue(config.isEnabled());
    }

    @Test
    public void shouldThrowExceptionOnInvalidJSONPropertyValue() throws CheckstyleException {

        publicThrown.expect(CheckstyleException.class);
        publicThrown.expectMessage("Exception while deserialising TMCConfiguration.");

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration2"));
    }
}
