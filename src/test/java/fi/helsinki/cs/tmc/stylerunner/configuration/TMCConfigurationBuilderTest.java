package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

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

import org.xml.sax.InputSource;

import static org.junit.Assert.*;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("fi.helsinki.cs.tmc.stylerunner.configuration.TMCConfigurationBuilder")
@PrepareForTest(LoggerFactory.class)
public class TMCConfigurationBuilderTest {

    private final Logger logger = mock(Logger.class);

    @Before
    public void setUp() {

        Whitebox.setInternalState(TMCConfigurationBuilder.class, logger);
    }

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
    public void shouldSelectFirstJSONFileIfMultipleFilesExist() throws CheckstyleException {

        final TMCConfiguration config = TMCConfigurationBuilder.build(new File("test-projects/valid/trivial_with_configuration"));

        verify(logger).warn("Multiple TMC-configuration files found, using the first matching.");

        assertEquals("mooc-checkstyle.xml", config.getRule());
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

    @Test
    public void shouldSelectFirstCheckstyleConfigurationIfMultipleConfigurationsExist() throws CheckstyleException {

        mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(TMCConfiguration.class)).thenReturn(logger);

        final File projectDirectory = new File("test-projects/valid/maven_exercise_with_configuration");

        final TMCConfiguration config = TMCConfigurationBuilder.build(projectDirectory);
        final InputSource inputSource = config.getInputSource(projectDirectory);

        verify(logger).warn("Multiple Checkstyle-configuration files found, using the first matching.");

        assertNotNull(inputSource);
    }
}
