package fi.helsinki.cs.tmc.stylerunner.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.xml.sax.InputSource;

public final class TMCConfiguration {

    private final boolean enabled;
    private final String rule;

    @JsonIgnore
    private File projectDirectory;

    public TMCConfiguration() {

        // Default configuration
        enabled = true;
        rule = "default-checkstyle.xml";
    }

    public boolean isEnabled() {

        return enabled;
    }

    public String getRule() {

        return rule;
    }

    public void setProjectDirectory(final File projectDirectory) {

        this.projectDirectory = projectDirectory;
    }

    public InputSource getInputSource() throws CheckstyleException {

        final Collection<File> matchingFiles = FileUtils.listFiles(projectDirectory,
                                                                   FileFilterUtils.nameFileFilter(rule),
                                                                   TrueFileFilter.INSTANCE);

        try {
            return new InputSource(new FileInputStream(new ArrayList<File>(matchingFiles).get(0)));
        } catch (FileNotFoundException exception) {
            throw new CheckstyleException("Exception while loading checkstyle configuration.", exception);
        }
    }
}
