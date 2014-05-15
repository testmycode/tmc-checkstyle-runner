package fi.helsinki.cs.tmc.stylerunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public final class MainTest {

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

        System.out.println("Hello");
        assertEquals("Hello\n", stdout.toString());
    }
}
