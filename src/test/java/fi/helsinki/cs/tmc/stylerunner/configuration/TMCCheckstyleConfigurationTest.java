package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TMCCheckstyleConfiguration.class, LoggerFactory.class })
public class TMCCheckstyleConfigurationTest {

    @Test
    public void shouldReturnDefaultConfigurationOnNonExistentConfigurationFile() throws CheckstyleException {

        mockStatic(LoggerFactory.class);
        final Logger logger = mock(Logger.class);
        when(LoggerFactory.getLogger(TMCCheckstyleConfiguration.class)).thenReturn(logger);

        final File projectDirectory = new File("test-projects/invalid/trivial_with_configuration3/");

        final TMCCheckstyleConfiguration configuration = TMCCheckstyleConfigurationBuilder.build(projectDirectory);

        assertEquals("unknown-checkstyle.xml", configuration.getRule());

        configuration.getInputSource(projectDirectory);

        verify(logger).error("Configuration file not found, using default configuration.");
    }
}
