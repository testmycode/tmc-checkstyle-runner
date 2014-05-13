package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;

public final class Main {

    private Main() {}

    private static void printUsage() {

        System.out.println("Usage: PATH");
    }

    private static void exitWithException(final Exception exception) {

        System.err.println(exception.getMessage());
        System.exit(1);
    }

    private static void validateArguments(final String[] args) {

        // No arguments
        if (args.length == 0) {
            printUsage();
            System.exit(0);
        }

        // Wrong amount of arguments
        if (args.length != 1) {
            System.err.println("Wrong amount or arguments.");
            System.exit(1);
        }
    }

    public static void main(final String[] args) {

        validateArguments(args);

        final File projectDirectory = new File(args[0]);

        try {
            new CheckstyleRunner(projectDirectory).run();
        } catch (CheckstyleException exception) {
            exitWithException(exception);
        }
    }
}
