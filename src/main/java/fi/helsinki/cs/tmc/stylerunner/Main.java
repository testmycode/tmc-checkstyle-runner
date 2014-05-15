
package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;



public final class Main {

    private String resultsFilename;
    private String projectDirectory;

    private Main() {}

    private static void printUsage() {

        final PrintStream out = System.out;
        out.println("Incorrect usage!");
        // TODO: tartteeko:
        out.println("1. Give as parameters a list of test methods with points like");
        out.println("  \"fully.qualified.ClassName.methodName{point1,point2,etc}\"");
        out.println();
        out.println("2. Define the following properties (java -Dprop=value)");
        out.println("  tmc.project_dir    The place to read source files from.");
        out.println("  tmc.validations_file    A file to write validation results to.");
        out.println();
    }

    public static void main(final String[] args) {
        try {
            new Main().run(args);
        } catch (IOException t) {
            System.err.print("Uncaught exception in main thread: ");
            t.printStackTrace(System.err);
        }
        // Ensure non-daemon threads exit
        System.exit(0);
    }

    private void run(final String[] args) throws IOException {

        try {
            readProperties();
            final File projectDirectory = new File(projectDirectory);
            final CheckstyleResult results = new CheckstyleRunner(projectDirectory).run();
            writeResults(results);
        } catch (CheckstyleException exception) {
            exitWithException(exception);
        } catch (IllegalArgumentException exception) {
            exitWithException(exception);
        }

    }

    private static void exitWithException(final Exception exception) {
        System.out.println(exception.getMessage());
        System.out.println();
        printUsage();
        System.exit(1);
    }

    private void readProperties() {

        resultsFilename = requireProperty("tmc.validations_file");
        projectDirectory = requireProperty("tmc.project_dir");
    }

    private String requireProperty(final String name) {

        final String prop = System.getProperty(name);
        if (prop != null) {
            return prop;
        } else {
            throw new IllegalArgumentException("Missing property: " + name);
        }
    }

    private void writeResults(final CheckstyleResult results) throws IOException {

        results.writeToJsonFile(new File(resultsFilename));
    }
}
