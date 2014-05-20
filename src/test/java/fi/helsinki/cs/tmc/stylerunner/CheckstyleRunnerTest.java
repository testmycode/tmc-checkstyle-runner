package fi.helsinki.cs.tmc.stylerunner;

import com.google.common.io.Files;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectException;

import static org.junit.Assert.*;

public class CheckstyleRunnerTest {

    @Rule
    public ExpectException publicThrown = ExpectException.none();

    @Test
    public void getSoureDirectoryTest() {


        try {

            tryAnt();
            tryMaven();
            tryNotOk();

        } catch (NoSuchMethodException ex) {
            fail(ex.getMessage());
        } catch (IllegalAccessException ex) {
            fail(ex.getMessage());
        } catch (InvocationTargetException ex) {
            fail(ex.getMessage());
        }
    }

    private void tryAnt() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = createAntProjectMock();
        try {
            method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
        } catch (CheckstyleException e) {
            fail();
        }
    }

    private void tryMaven() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = createMavenProjectMock();
        try {
            method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
        } catch (CheckstyleException e) {
            fail();
        }
    }

    private void tryNotOk() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        final Method method = CheckstyleRunner.class.getDeclaredMethod("getSourceDirectory", File.class);
        method.setAccessible(true);
        final File tmpDir = Files.createTempDir();
        boolean hadError = false;
        try {
            method.invoke(new CheckstyleRunner(tmpDir), tmpDir);
        } catch (CheckstyleException e) {
            hadError = true;
        }

        assertTrue(hadError);
    }

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
            final File pomFile = new File(tmpDir + File.separator + "pom.xml");

            Files.createParentDirs(srcJavaFile);
            Files.createParentDirs(testJavaFile);

            return tmpDir;
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        return null;
    }
//    @Test
//    public void testRun() {
//        CheckstyleRunner instance = null;
//        CheckstyleResult expResult = null;
//        CheckstyleResult result = instance.run();
//        assertEquals(expResult, result);
//
//    }
//
//    /**
//     * Test of run method, of class CheckstyleRunner.
//     */
//    @Test
//    public void testRun_File() {
//        System.out.println("run");
//        File output = null;
//        CheckstyleRunner instance = null;
//        instance.run(output);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
}
