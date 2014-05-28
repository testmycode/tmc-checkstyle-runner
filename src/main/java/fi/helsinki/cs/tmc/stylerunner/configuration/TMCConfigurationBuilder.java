package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.IOException;

public final class TMCConfigurationBuilder {

    private static final String TMC_CONFIGURATION = "tmc.json";

    private TMCConfigurationBuilder() {}

    private static File getConfigurationFile(final File projectDirectory) {

        // Find TMC-configuration from project
        final File matchingFile = new File(projectDirectory, TMC_CONFIGURATION);

        // No TMC-configuration file found
        if (!matchingFile.exists()) {
            return null;
        }

        return matchingFile;
    }

    public static TMCConfiguration build(final File projectDirectory) throws CheckstyleException {

        final File configurationFile = getConfigurationFile(projectDirectory);

        // No TMC-configuration found, use default
        if (configurationFile == null) {
            return new TMCConfiguration();
        }

        try {

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final JsonNode rootNode = mapper.readTree(configurationFile);

            return mapper.treeToValue(rootNode.path("checkstyle"), TMCConfiguration.class);

        } catch (IOException exception) {
            throw new CheckstyleException("Exception while deserialising TMCConfiguration.", exception);
        }
    }
}
