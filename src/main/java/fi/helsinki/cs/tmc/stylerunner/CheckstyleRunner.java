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
import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;

public final class CheckstyleRunner {

    private final Checker checker;
    private final List<File> files;

    public CheckstyleRunner(final File projectDirectory) throws CheckstyleException {

        // Default configuration
        final InputSource inputSource = new InputSource(this.getClass()
                                                            .getClassLoader()
                                                            .getResourceAsStream("default-checkstyle.xml"));

        final Configuration config = ConfigurationLoader.loadConfiguration(inputSource,
                                                                           new PropertiesExpander(System.getProperties()),
                                                                           false);

        checker = new Checker();

        // Get all .java files from project directory
        files = (List<File>) FileUtils.listFiles(projectDirectory, new String[] { "java" }, true);

        // Configuration
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
