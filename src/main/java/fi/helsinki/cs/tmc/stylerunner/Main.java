package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;


import java.io.File;
import java.io.PrintStream;

public final class Main {

    private Main() {}

    private static void printUsage() {

        final PrintStream out = System.out;
        out.println("Incorrect usage!");

        out.println("1. Give parameters as a list of test methods with points like");
        out.println("  \"fully.qualified.ClassName.methodName{point1,point2,etc}\"");
        out.println();
        out.println("2. Define the following properties (java -Dprop=value)");
        out.println("  tmc.project_dir    The place to read source files from.");
        out.println("  tmc.validations_file    A file to write validation results to.");
        out.println();
    }

    private static void exitWithException(final Exception exception) {

        System.out.println(exception.getMessage());
        System.exit(1);
    }

    private static String requireProperty(final String name) {

        final String property = System.getProperty(name);

        if (property == null) {
            System.err.println("Missing property: " + name);
            printUsage();
            System.exit(0);
        }

        return property;
    }

    public static void main(final String[] args) {

        try {
            final File projectFile = new File(requireProperty("tmc.project_dir"));
            new CheckstyleRunner(projectFile).run(new File(requireProperty("tmc.validations_file")));
        } catch (CheckstyleException exception) {
            exitWithException(exception);
        } catch (IllegalArgumentException exception) {
            exitWithException(exception);
        }
    }
}
