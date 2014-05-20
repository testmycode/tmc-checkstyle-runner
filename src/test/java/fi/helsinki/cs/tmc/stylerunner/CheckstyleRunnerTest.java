package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CheckstyleRunnerTest {

    @Rule
    public ExpectedException publicThrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionOnInvalidProjectDirectory() throws CheckstyleException {

        publicThrown.expect(CheckstyleException.class);
        publicThrown.expectMessage("Path does not contain a testable project.");

        new CheckstyleRunner(new File("src/"));
    }

    @Test
    public void shouldRunOnValidProjectDirectory() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File(".")).run();
        assertNotNull(result);
    }

    @Test
    public void shouldNotHaveErrors() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File(".")).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }
}
