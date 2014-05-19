package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.PrintStream;

public final class Main {

    private Main() {}

    private static void printUsage() {

        final PrintStream stdout = System.out;

        stdout.println("Usage:");
        stdout.println("Properties (java -Dproperty=value)");
        stdout.println("  tmc.project_dir — The path for the project directory.");
        stdout.println("  tmc.validations_file — A path to a file to write the validation results.");
    }

    private static void exitWithException(final Exception exception) {

        System.err.println(exception.getMessage());
        System.exit(1);
    }

    private static String requireProperty(final String name) {

        final String property = System.getProperty(name);

        if (property == null) {
            System.err.println("Missing property: " + name + "\n");
            printUsage();
            System.exit(0);
        }

        return property;
    }

    public static void main(final String[] args) {

        try {
            final File projectDirectory = new File(requireProperty("tmc.project_dir"));
            final File output = new File(requireProperty("tmc.validations_file"));
            new CheckstyleRunner(projectDirectory).run(output);
        } catch (CheckstyleException exception) {
            exitWithException(exception);
        } catch (IllegalArgumentException exception) {
            exitWithException(exception);
        }
    }
}
