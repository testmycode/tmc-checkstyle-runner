package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;

public final class Main {

    private Main() {}

    public static void main(String[] args) throws CheckstyleException {

        new CheckstyleRunner(new File(".")).run();
    }
}
