package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Main {

    private Main() {}

    public static void main(String[] args) throws CheckstyleException {

        final List<File> files = new ArrayList<File>();
        files.add(new File("./src/main/java/fi/helsinki/cs/tmc/stylerunner/CheckstyleRunner.java"));

        new CheckstyleRunner(files).run();
    }
}
