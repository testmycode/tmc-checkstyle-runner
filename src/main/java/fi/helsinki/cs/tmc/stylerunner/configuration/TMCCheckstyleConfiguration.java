package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.xml.sax.InputSource;

public final class TMCCheckstyleConfiguration {

    private static final String DEFAULT_CHECKSTYLE_CONFIGURATION = "default-checkstyle.xml";

    private final boolean enabled;
    private final String rule;

    public TMCCheckstyleConfiguration() {

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
        final File configuration = new File(projectDirectory, rule);

        try {
            return new InputSource(new FileInputStream(configuration));
        } catch (FileNotFoundException exception) {
            throw new CheckstyleException("Exception while loading Checkstyle-configuration.", exception);
        }
    }
}
