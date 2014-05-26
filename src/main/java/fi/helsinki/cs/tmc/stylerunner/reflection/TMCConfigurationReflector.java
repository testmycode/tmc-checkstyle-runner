package fi.helsinki.cs.tmc.stylerunner.reflection;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TMCConfigurationReflector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TMCConfigurationReflector.class);

    private TMCConfigurationReflector() {}

    private static File getConfigurationFile(final File projectDirectory) {

        final Collection<File> matchingFiles = FileUtils.listFiles(projectDirectory,
                                                                   FileFilterUtils.nameFileFilter("TMCConfig.java"),
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

    public static TMCConfiguration reflect(final File projectDirectory) {

        final File configurationFile = getConfigurationFile(projectDirectory);

        // No configuration file found
        if (configurationFile == null) {
            return null;
        }

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        final Iterable<? extends JavaFileObject> files = Arrays.asList(new FileJavaFileObject(configurationFile));
        final Boolean succeeded = compiler.getTask(null, fileManager, null, null, null, files).call();

        System.out.println(succeeded);

        try {
            final Class configurationClass = fileManager.getClassLoader(StandardLocation.CLASS_PATH).loadClass("TMCConfig");
            System.out.println(configurationClass);
        } catch (ClassNotFoundException exception) {
            System.err.println(exception);
        }

        return null;
    }
}
