package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    private TMCConfigurationBuilder() {}

    private static File getConfigurationFile(final File projectDirectory) {

        final Collection<File> matchingFiles = FileUtils.listFiles(projectDirectory,
                                                                   FileFilterUtils.nameFileFilter("tmc.json"),
                                                                   TrueFileFilter.INSTANCE);

        // No configuration file found
        if (matchingFiles.isEmpty()) {
            return null;
        }

        if (matchingFiles.size() > 1) {
            LOGGER.warn("Multiple configuration files found, using the first matching.");
        }

        return new ArrayList<File>(matchingFiles).get(0);
    }

    public static TMCConfiguration build(final File projectDirectory) throws IOException {

        final File configuration = getConfigurationFile(projectDirectory);

        return new ObjectMapper().readValue(configuration, TMCConfiguration.class);
    }
}
