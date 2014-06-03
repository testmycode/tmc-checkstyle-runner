package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

import fi.helsinki.cs.tmc.stylerunner.configuration.TMCCheckstyleConfiguration;
import fi.helsinki.cs.tmc.stylerunner.configuration.TMCCheckstyleConfigurationBuilder;
import fi.helsinki.cs.tmc.stylerunner.listener.CheckstyleResultListener;
import fi.helsinki.cs.tmc.stylerunner.validation.CheckstyleResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import org.xml.sax.InputSource;

public final class CheckstyleRunner {

    private final TMCCheckstyleConfiguration checkstyleConfiguration;
    private final Checker checker = new Checker();
    private final List<File> files;

    public CheckstyleRunner(final File projectDirectory) throws CheckstyleException {

        // Get source directory and check that the project is testable
        final File sourceDirectory = getSourceDirectory(projectDirectory);

        checkstyleConfiguration = TMCCheckstyleConfigurationBuilder.build(projectDirectory);
        final InputSource inputSource = checkstyleConfiguration.getInputSource(projectDirectory);

        final Configuration config = ConfigurationLoader.loadConfiguration(inputSource,
                                                                           new PropertiesExpander(System.getProperties()),
                                                                           false);

        // Get all .java files from project’s source directory
        files = (List<File>) FileUtils.listFiles(sourceDirectory, new String[]{ "java" }, true);

        // Configuration
        checker.setModuleClassLoader(Checker.class.getClassLoader());
        checker.configure(config);
        checker.setBasedir(sourceDirectory.getAbsolutePath());

        // Configure localisation locale
        LocalizedMessage.setLocale(Locale.ROOT);
    }

    private File getSourceDirectory(final File projectDirectory) throws CheckstyleException {

        // Ant-project
        File sourceDirectory = new File(projectDirectory, "src/");

        // Maven-project
        if (new File(projectDirectory, "pom.xml").exists()) {
            sourceDirectory = new File(projectDirectory, "src/main/java/");
        }

        // Invalid directory
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            throw new CheckstyleException("Path does not contain a testable project.");
        }

        return sourceDirectory;
    }

    public CheckstyleResult run() {

        // Checkstyle disabled, return empty result
        if (!checkstyleConfiguration.isEnabled()) {
            return new CheckstyleResult();
        }

        // Listener
        final CheckstyleResultListener listener = new CheckstyleResultListener();
        checker.addListener(listener);

        // Process
        checker.process(files);

        // Clean up
        checker.destroy();

        return listener.getResult();
    }

    public void run(final File outputFile) throws CheckstyleException {

        final CheckstyleResult result = run();

        if (outputFile.exists()) {
            throw new CheckstyleException("Output file already exists.");
        }

        try {
            result.writeToFile(outputFile);
        } catch (IOException exception) {
            throw new CheckstyleException("Exception while writing to output file.", exception);
        }
    }
}
