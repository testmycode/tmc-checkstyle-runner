package fi.helsinki.cs.tmc.stylerunner.configuration;

import fi.helsinki.cs.tmc.stylerunner.exception.TMCCheckstyleException;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import static org.mockito.Mockito.verify;

import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("fi.helsinki.cs.tmc.stylerunner.configuration.TMCCheckstyleConfigurationBuilder")
@PrepareForTest({TMCCheckstyleConfigurationBuilder.class, LoggerFactory.class })
public class TMCCheckstyleConfigurationBuilderTest {

    private Logger logger;
    private String configuration;

    @Before
    public void setUp() {

        configuration = new String("tmc.json");
        Whitebox.setInternalState(TMCCheckstyleConfigurationBuilder.class, configuration);

        logger = mock(Logger.class);
        Whitebox.setInternalState(TMCCheckstyleConfigurationBuilder.class, logger);
    }

    @Test
    public void shouldReturnDefaultTMCConfigurationOnNonexistentConfiguration() throws TMCCheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/valid/trivial/"));

        assertNotNull(config);
    }

    @Test
    public void shouldReturnTMCConfiguration() throws TMCCheckstyleException, FileNotFoundException {

        final File projectDirectory = new File("test-projects/valid/trivial_with_configuration/");
        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(projectDirectory);

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
        assertNotNull(config.getInputSource(projectDirectory));
    }

    @Test
    public void shouldNotFailOnAdditionalAndInvalidJSONProperties() throws TMCCheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration/"));

        assertNotNull(config);
        assertEquals("default-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldNotFailOnAdditionalJSONRootProperties() throws TMCCheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/maven_exercise_with_configuration/"));

        assertNotNull(config);
        assertEquals("mooc-checkstyle.xml", config.getRule());
        assertFalse(config.isEnabled());
    }

    @Test
    public void shouldReturnDefaultTMCConfigurationIfConfigurationIsInvalid() throws TMCCheckstyleException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/maven_with_configuration/"));

        assertEquals("default-checkstyle.xml", config.getRule());
        assertTrue(config.isEnabled());
    }

    @Test
    public void shouldReturnDefaultConfigurationOnInvalidJSONPropertyValue() throws TMCCheckstyleException, IllegalAccessException {

        final TMCCheckstyleConfiguration config = TMCCheckstyleConfigurationBuilder.build(new File("test-projects/invalid/trivial_with_configuration2/"));

        verify(logger).error("Exception while deserialising TMCConfiguration.");
    }
}
