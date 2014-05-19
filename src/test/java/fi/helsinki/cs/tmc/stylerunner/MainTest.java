package fi.helsinki.cs.tmc.stylerunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

import static org.junit.Assert.*;

public final class MainTest {

    @Rule
    public final ExpectedSystemExit publicExit = ExpectedSystemExit.none();

    private final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    private final ByteArrayOutputStream stderr = new ByteArrayOutputStream();

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

        publicExit.checkAssertionAfterwards(new Assertion() {

            @Override
            public void checkAssertion() {

                final String expected = "Usage:\n" +
                                        "Properties (java -Dproperty=value)\n" +
                                        "  tmc.project_dir — The path for the project directory.\n" +
                                        "  tmc.validations_file — A path to a file to write the validation results.\n";

                assertEquals(expected, stdout.toString());
            }
        });

        Main.main(new String[0]);
    }
}
