package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.IOException;

public final class TMCCheckstyleConfigurationBuilder {

    // Create string as object instead of literal for testing purposes
    private static final String TMC_CONFIGURATION = new String("tmc.json");

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

    public static TMCCheckstyleConfiguration build(final File projectDirectory) throws CheckstyleException {

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
            throw new CheckstyleException("Exception while deserialising TMCConfiguration.", exception);
        }
    }
}
