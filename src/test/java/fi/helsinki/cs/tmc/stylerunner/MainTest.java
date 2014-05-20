package fi.helsinki.cs.tmc.stylerunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import static org.junit.Assert.*;

public final class MainTest {

    @Rule
    public final ExpectedSystemExit publicExit = ExpectedSystemExit.none();

    @Rule
    public final RestoreSystemProperties publicDirProperty = new RestoreSystemProperties("tmc.project_dir");

    @Rule
    public final RestoreSystemProperties publicValidationsProperty = new RestoreSystemProperties("tmc.validations_file");

    private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    private final ByteArrayOutputStream stderr = new ByteArrayOutputStream();

    private Assertion createAssertionForUsage() {

        return new Assertion() {

            @Override
            public void checkAssertion() {

                final String expected = "Usage:\n" +
                                        "Properties (java -Dproperty=value)\n" +
                                        "  tmc.project_dir — The path for the project directory.\n" +
                                        "  tmc.validations_file — A path to a file to write the validation results.\n";

                assertEquals(expected, stdout.toString());
            }
        };
    }

    private void readAndDeleteOutputFile() throws FileNotFoundException {

        final File file = new File("target/output.txt");

        assertTrue(file.exists());

        final Scanner scanner = new Scanner(file);
        final String line = scanner.nextLine();

        try {
            assertEquals("{\"validationErrors\":{}}", line);
        } finally {
            file.delete();
        }
    }

    @Before
    public void setUp() {

        System.setOut(new PrintStream(stdout));
        System.setErr(new PrintStream(stderr));
    }

    @After
    public void tearDown() {

        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void shouldPrintUsageOnNoArguments() {

        publicExit.expectSystemExitWithStatus(0);
        publicExit.checkAssertionAfterwards(createAssertionForUsage());

        Main.main(new String[0]);
    }

    @Test
    public void shouldPrintUsageOnInvalidProperties() {

        publicExit.expectSystemExitWithStatus(0);
        publicExit.checkAssertionAfterwards(createAssertionForUsage());

        System.setProperty("tmc.invalid", "valid");

        Main.main(new String[0]);
    }

    @Test
    public void shouldCreateJsonFileWithCorrectProperties() throws FileNotFoundException {

        System.setProperty("tmc.project_dir", ".");
        System.setProperty("tmc.validations_file", "target/output.txt");

        Main.main(new String[0]);

        readAndDeleteOutputFile();
    }

    @Test
    public void shouldCreateJsonFileOnAdditionalInvalidProperties() throws FileNotFoundException {

        System.setProperty("tmc.project_dir", ".");
        System.setProperty("tmc.validations_file", "target/output.txt");
        System.setProperty("tmc.invalid", "valid");

        Main.main(new String[0]);

        readAndDeleteOutputFile();
    }

    @Test
    public void shouldExitWithExceptionWithIncorrectProjectDirectory() {

        publicExit.expectSystemExitWithStatus(1);

        publicExit.checkAssertionAfterwards(new Assertion() {

            @Override
            public void checkAssertion() {

                final String expected = "Path does not contain a testable project.\n";

                assertEquals(expected, stderr.toString());
            }
        });

        System.setProperty("tmc.project_dir", "nonexistent");
        System.setProperty("tmc.validations_file", "target/output.txt");

        Main.main(new String[0]);
    }

    @Test
    public void shouldThrowExceptionWithIncorrectValidationsFilePath() {

        System.setProperty("tmc.project_dir", ".");
        System.setProperty("tmc.validations_file", "nonexistent/output.txt");

        Main.main(new String[0]);

        final String expected = "nonexistent/output.txt (No such file or directory)\n";

        assertEquals(expected, stderr.toString());
    }

    @Test
    public void shouldNotThrowAnyExceptionsWhenRunningMain() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        final Constructor constructor = Main.class.getDeclaredConstructor((Class<?>[]) null);
        constructor.setAccessible(true);
        constructor.newInstance(null);
    }
}
