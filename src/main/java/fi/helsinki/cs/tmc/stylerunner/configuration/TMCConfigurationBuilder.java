package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TMCConfigurationBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(TMCConfigurationBuilder.class);
    private static final String TMC_CONFIGURATION = "tmc.json";

    private TMCConfigurationBuilder() {}

    private static File getConfigurationFile(final File projectDirectory) {

        // Find TMC-configuration from project
        final Collection<File> matchingFiles = FileUtils.listFiles(projectDirectory,
                                                                   FileFilterUtils.nameFileFilter(TMC_CONFIGURATION),
                                                                   TrueFileFilter.INSTANCE);

        // No TMC-configuration file found
        if (matchingFiles.isEmpty()) {
            return null;
        }

        if (matchingFiles.size() > 1) {
            LOGGER.warn("Multiple TMC-configuration files found, using the first matching.");
        }

        return new ArrayList<File>(matchingFiles).get(0);
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
            final TMCConfiguration configuration = mapper.treeToValue(rootNode.path("checkstyle"),
                                                                      TMCConfiguration.class);

            return configuration;

        } catch (IOException exception) {
            throw new CheckstyleException("Exception while deserialising TMCConfiguration.", exception);
        }
    }
}
