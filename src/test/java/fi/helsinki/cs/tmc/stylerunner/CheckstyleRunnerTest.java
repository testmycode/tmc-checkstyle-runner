package fi.helsinki.cs.tmc.stylerunner;

import com.google.common.io.Files;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import fi.helsinki.cs.tmc.stylerunner.configuration.TMCCheckstyleConfigurationBuilder;
import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationError;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CheckstyleRunnerTest {

    @Rule
    public ExpectedException publicThrown = ExpectedException.none();

    private File createAntProjectMock() {

        try {

            final File tmp = Files.createTempDir();
            final File srcFile = new File(tmp, "src/Main.java");
            Files.createParentDirs(srcFile);

            return tmp;

        } catch (IOException exception) {
            fail(exception.getMessage());
        }

        return null;
    }

    private File createMavenProjectMock() {

        try {

            final File tmp = Files.createTempDir();
            final File srcFile = new File(tmp, "src/Main/Main.java");
            final File testFile = new File(tmp, "src/Test/MainTest.java");

            Files.createParentDirs(srcFile);
            Files.createParentDirs(testFile);

            return tmp;

        } catch (IOException exception) {
            fail(exception.getMessage());
        }

        return null;
    }

    @Test
    public void shouldWorkWithAntProjects() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final File tmp = createAntProjectMock();

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);

        try {
            method.invoke(new CheckstyleRunner(tmp, Locale.ROOT), tmp);
        } catch (CheckstyleException exception) {
            fail();
        }
    }

    @Test
    public void shouldWorkWithMavenProjects() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final File tmp = createMavenProjectMock();

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);

        try {
            method.invoke(new CheckstyleRunner(tmp, Locale.ROOT), tmp);
        } catch (CheckstyleException e) {
            fail();
        }
    }

    @Test
    public void shouldNotWorkWhenDirectoryNotInCorrectFormat() throws IllegalAccessException,
                                                                      InvocationTargetException,
                                                                      NoSuchMethodException,
                                                                      CheckstyleException {

        final File tmp = Files.createTempDir();

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);

        publicThrown.expect(CheckstyleException.class);
        publicThrown.expectMessage("Path does not contain a testable project.");

        method.invoke(new CheckstyleRunner(tmp, Locale.ROOT), tmp);
    }

    @Test
    public void shouldThrowExceptionOnInvalidProjectDirectory() throws CheckstyleException {

        publicThrown.expect(CheckstyleException.class);
        publicThrown.expectMessage("Path does not contain a testable project.");

        new CheckstyleRunner(new File("src/"), Locale.ROOT);
    }

    @Test
    public void shouldRunOnValidProjectDirectory() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("."), Locale.ROOT).run();
        assertNotNull(result);
    }

    @Test
    public void shouldNotHaveValidationErrors() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/valid/trivial/"), Locale.ROOT).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldNotHaveValidationErrorsOnAntTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/valid/trivial/"), Locale.ROOT).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveValidationErrorsOnAntTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/trivial/"), Locale.ROOT).run();
        assertFalse(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErroneousClassOnAntTestProject() throws CheckstyleException {

        final File testProject = new File("test-projects/invalid/trivial/");
        final CheckstyleResult result = new CheckstyleRunner(testProject, Locale.ROOT).run();

        final List<ValidationError> errors = result.getValidationErrors().get(new File("Trivial.java"));

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals(2, errors.size());

        String expected = "Indentation incorrect. Expected 4, but was 5.";

        assertEquals("com.puppycrawl.tools.checkstyle.checks.indentation.IndentationCheck", errors.get(0).getSourceName());
        assertEquals(4, errors.get(0).getLine());
        assertEquals(0, errors.get(0).getColumn());
        assertEquals(expected, errors.get(0).getMessage());

        expected = "Indentation incorrect. Expected 8, but was 9.";

        assertEquals("com.puppycrawl.tools.checkstyle.checks.indentation.IndentationCheck", errors.get(1).getSourceName());
        assertEquals(5, errors.get(1).getLine());
        assertEquals(0, errors.get(1).getColumn());
        assertEquals(expected, errors.get(1).getMessage());
    }

    @Test
    public void shouldNotHaveValidationErrorsOnMavenTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/valid/maven_exercise/"), Locale.ROOT).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveValidationErrorsOnMavenTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/maven_exercise/"), Locale.ROOT).run();
        assertFalse(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErroneousClassOnMavenTestProject() throws CheckstyleException {

        final File testProject = new File("test-projects/invalid/maven_exercise/");
        final CheckstyleResult result = new CheckstyleRunner(testProject, Locale.ROOT).run();

        final List<ValidationError> errors = result.getValidationErrors().get(new File("fi/helsinki/cs/maventest/App.java"));

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals(1, errors.size());

        final String expected = "Indentation incorrect. Expected 4, but was 5.";

        assertEquals("com.puppycrawl.tools.checkstyle.checks.indentation.IndentationCheck", errors.get(0).getSourceName());
        assertEquals(4, errors.get(0).getLine());
        assertEquals(0, errors.get(0).getColumn());
        assertEquals(expected, errors.get(0).getMessage());
    }

    @Test
    public void shouldReturnEmptyCheckstyleResultWhenCheckstyleIsDisabled() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/trivial_with_configuration/"), Locale.ROOT).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldReturnValidationErrorsWhenCheckstyleIsEnabled() throws CheckstyleException, NoSuchFieldException, IllegalAccessException {

        setFinalStatic(TMCCheckstyleConfigurationBuilder.class.getDeclaredField("TMC_CONFIGURATION"), "tmc-enabled.json");

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/trivial_with_configuration/"), Locale.ROOT).run();

        final List<ValidationError> errors = result.getValidationErrors().get(new File("Trivial.java"));

        assertFalse(result.getValidationErrors().isEmpty());

        assertEquals(2, errors.size());
        assertEquals(2, errors.get(0).getLine());
        assertEquals(0, errors.get(0).getColumn());
        assertEquals("Missing package declaration.", errors.get(0).getMessage());

        assertEquals(2, errors.get(1).getLine());
        assertEquals(22, errors.get(1).getColumn());
        assertEquals("'{' is not preceded with whitespace.", errors.get(1).getMessage());

        setFinalStatic(TMCCheckstyleConfigurationBuilder.class.getDeclaredField("TMC_CONFIGURATION"), "tmc.json");
    }

    private static void setFinalStatic(final Field field, final Object newValue) throws IllegalAccessException, NoSuchFieldException {

        field.setAccessible(true);

        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }
}
