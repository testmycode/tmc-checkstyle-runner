package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public final class CheckstyleRunner {

    private final Configuration config;
    private final Checker checker;
    private final List<File> files;

    public CheckstyleRunner(final List<File> files) throws CheckstyleException {

        config = ConfigurationLoader.loadConfiguration("./src/main/resources/default-checkstyle.xml",
                                                            new PropertiesExpander(System.getProperties()));
        checker = new Checker();
        this.files = files;

        checker.setModuleClassLoader(Checker.class.getClassLoader());
        checker.configure(config);
    }

    public void run() throws CheckstyleException {

        // Listener
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final AuditListener listener = new DefaultLogger(output, false);
        checker.addListener(listener);

        // Process
        final int errors = checker.process(files);
        System.out.println("Number of errors: " + errors);
        System.out.println(output.toString());

        // Clean up
        checker.destroy();
    }
}
