package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TMCCheckstyleConfigurationBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TMCCheckstyleConfigurationBuilder.class);

    // Create string as object instead of literal for testing purposes
    private static final String TMC_CONFIGURATION = new String(".tmcproject.json");

    private TMCCheckstyleConfigurationBuilder() {}

    private static File getConfigurationFile(final File projectDirectory) {

        // Find TMC-configuration from project
        final File matchingFile = new File(projectDirectory, TMC_CONFIGURATION);

        // No TMC-configuration file found
        if (!matchingFile.exists()) {
            return null;
        }

        return matchingFile;
    }

    public static TMCCheckstyleConfiguration build(final File projectDirectory) {

        final File configurationFile = getConfigurationFile(projectDirectory);

        // No TMC-configuration found, use default
        if (configurationFile == null) {
            return new TMCCheckstyleConfiguration();
        }

        try {

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final JsonNode rootNode = mapper.readTree(configurationFile);

            // No Checkstyle-configuration found, use default
            if (rootNode.findValue("checkstyle") == null) {
                return new TMCCheckstyleConfiguration();
            }

            return mapper.treeToValue(rootNode.path("checkstyle"), TMCCheckstyleConfiguration.class);

        } catch (IOException exception) {

            LOGGER.error("Exception while deserialising TMCConfiguration.");
            return new TMCCheckstyleConfiguration();
        }
    }
}
