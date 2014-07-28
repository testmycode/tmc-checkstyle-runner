package fi.helsinki.cs.tmc.stylerunner.configuration;

import fi.helsinki.cs.tmc.stylerunner.exception.TMCCheckstyleException;
import fi.helsinki.cs.tmc.stylerunner.validation.Strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.InputSource;

public final class TMCCheckstyleConfiguration {

    private static final String DEFAULT_CHECKSTYLE_CONFIGURATION = "default-checkstyle.xml";

    private final Logger logger = LoggerFactory.getLogger(TMCCheckstyleConfiguration.class);

    private final String rule;
    private final Strategy strategy;

    public TMCCheckstyleConfiguration() {

        // Default configuration
        rule = DEFAULT_CHECKSTYLE_CONFIGURATION;
        strategy = Strategy.DISABLED;
    }

    public String getRule() {

        return rule;
    }

    public Strategy getStrategy() {

        return strategy;
    }

    public boolean isEnabled() {

        return strategy != Strategy.DISABLED;
    }

    public InputSource getInputSource(final File projectDirectory) throws TMCCheckstyleException {

        // Find Checkstyle-configuration from project
        final File configuration = new File(projectDirectory, rule);

        if (!configuration.exists()) {
            logger.error("Configuration file not found, using default configuration.");
        }

        // Use default Checkstyle-configuration
        if (!configuration.exists() || rule.equals(DEFAULT_CHECKSTYLE_CONFIGURATION)) {

            // Default configuration
            return new InputSource(this.getClass()
                                       .getClassLoader()
                                       .getResourceAsStream(DEFAULT_CHECKSTYLE_CONFIGURATION));
        }

        try {
            return new InputSource(new FileInputStream(configuration));
        } catch (FileNotFoundException exception) {
            throw new TMCCheckstyleException("Exception while loading Checkstyle-configuration.", exception);
        }
    }
}
