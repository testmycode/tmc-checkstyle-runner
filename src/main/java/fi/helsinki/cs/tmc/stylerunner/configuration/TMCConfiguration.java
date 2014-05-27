package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.InputSource;

public final class TMCConfiguration {

    private static final String DEFAULT_CHECKSTYLE_CONFIGURATION = "default-checkstyle.xml";

    private final Logger logger = LoggerFactory.getLogger(TMCConfiguration.class);
    private final boolean enabled;
    private final String rule;

    public TMCConfiguration() {

        // Default configuration
        enabled = true;
        rule = DEFAULT_CHECKSTYLE_CONFIGURATION;
    }

    public boolean isEnabled() {

        return enabled;
    }

    public String getRule() {

        return rule;
    }

    public InputSource getInputSource(final File projectDirectory) throws CheckstyleException {

        // Use default Checkstyle-configuration
        if (rule.equals(DEFAULT_CHECKSTYLE_CONFIGURATION)) {

            // Default configuration
            return new InputSource(this.getClass()
                                       .getClassLoader()
                                       .getResourceAsStream(DEFAULT_CHECKSTYLE_CONFIGURATION));
        }

        // Find Checkstyle-configuration from project
        final Collection<File> matchingFiles = FileUtils.listFiles(projectDirectory,
                                                                   FileFilterUtils.nameFileFilter(rule),
                                                                   TrueFileFilter.INSTANCE);

        if (matchingFiles.size() > 1) {
            logger.warn("Multiple Checkstyle-configuration files found, using the first matching.");
        }

        try {
            return new InputSource(new FileInputStream(new ArrayList<File>(matchingFiles).get(0)));
        } catch (FileNotFoundException exception) {
            throw new CheckstyleException("Exception while loading Checkstyle-configuration.", exception);
        }
    }
}
