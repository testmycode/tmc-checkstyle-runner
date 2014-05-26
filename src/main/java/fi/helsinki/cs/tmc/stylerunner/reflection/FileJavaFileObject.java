package fi.helsinki.cs.tmc.stylerunner.reflection;

import java.io.File;
import java.io.IOException;

import javax.tools.SimpleJavaFileObject;

import org.apache.commons.io.FileUtils;

public final class FileJavaFileObject extends SimpleJavaFileObject {

    private final File file;

    public FileJavaFileObject(final File file) {

        super(file.toURI(), Kind.SOURCE);

        this.file = file;
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {

        return FileUtils.readFileToString(file);
    }
}
