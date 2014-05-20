package fi.helsinki.cs.tmc.stylerunner;

import com.google.common.io.Files;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;
import fi.helsinki.cs.tmc.stylerunner.validation.ValidationError;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CheckstyleRunnerTest {

    @Rule
    public ExpectedException publicThrown = ExpectedException.none();

    private File createAntProjectMock() {

        try {

            final File tmpDir = Files.createTempDir();
            final File srcJavaFile = new File(tmpDir + File.separator + "src/Main.java");
            Files.createParentDirs(srcJavaFile);

            return tmpDir;

        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        return null;
    }

    private File createMavenProjectMock() {

        try {

            final File tmpDir = Files.createTempDir();
            final File srcJavaFile = new File(tmpDir + File.separator + "src/Main/Main.java");
            final File testJavaFile = new File(tmpDir + File.separator + "src/Test/MainTest.java");

            Files.createParentDirs(srcJavaFile);
            Files.createParentDirs(testJavaFile);

            return tmpDir;

        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        return null;
    }

    @Test
    public void shouldWorkWithAntProjects() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = createAntProjectMock();

        try {
            method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
        } catch (CheckstyleException e) {
            fail();
        }
    }

    @Test
    public void shouldWorkWithMavenProjects() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = createMavenProjectMock();

        try {
            method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
        } catch (CheckstyleException e) {
            fail();
        }
    }

    @Test
    public void shouldNotWorkWhenDirNotInCorrectFormat() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, CheckstyleException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = Files.createTempDir();

        publicThrown.expect(CheckstyleException.class);
        publicThrown.expectMessage("Path does not contain a testable project.");

        method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
    }

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

    @Test
    public void shouldNotHaveErrorsOnAntTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/valid/trivial")).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorsOnAntTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/trivial")).run();
        assertFalse(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErroneousClassOnAntTestProject() throws CheckstyleException {

        final File testProject = new File("test-projects/invalid/trivial");
        final CheckstyleResult result = new CheckstyleRunner(testProject).run();

        final List<ValidationError> errors = result.getValidationErrors().get(new File(testProject.getAbsolutePath(),
                                                                              "src/Trivial.java"));

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals(2, errors.size());

        String expected = "method def modifier at indentation level 5 not at correct indentation, 4";
        assertEquals(expected, errors.get(0).getMessage());
        assertEquals(4, errors.get(0).getLine());

        expected = "method def child at indentation level 9 not at correct indentation, 8";
        assertEquals(expected, errors.get(1).getMessage());
        assertEquals(5, errors.get(1).getLine());
    }

    @Test
    public void shouldNotHaveErrorsOnMavenTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/valid/maven_exercise")).run();
        assertTrue(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErrorsOnMavenTestProject() throws CheckstyleException {

        final CheckstyleResult result = new CheckstyleRunner(new File("test-projects/invalid/maven_exercise")).run();
        assertFalse(result.getValidationErrors().isEmpty());
    }

    @Test
    public void shouldHaveErroneousClassOnMavenTestProject() throws CheckstyleException {

        final File testProject = new File("test-projects/invalid/maven_exercise");
        final CheckstyleResult result = new CheckstyleRunner(testProject).run();

        final List<ValidationError> errors = result.getValidationErrors().get(new File(testProject.getAbsolutePath(),
                                                                              "src/main/java/fi/helsinki/cs/maventest/App.java"));

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals(1, errors.size());

        final String expected = "method def modifier at indentation level 5 not at correct indentation, 4";
        assertEquals(expected, errors.get(0).getMessage());
        assertEquals(4, errors.get(0).getLine());
    }
}
