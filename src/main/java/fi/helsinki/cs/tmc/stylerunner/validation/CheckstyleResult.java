package fi.helsinki.cs.tmc.stylerunner.validation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CheckstyleResult implements ValidationResult {

    private final Map<File, List<ValidationError>> validationErrors = new HashMap<File, List<ValidationError>>();

    public void addError(final AuditEvent auditEvent) {

        final File file = new File(auditEvent.getFileName());

        if (!validationErrors.containsKey(file)) {
            validationErrors.put(file, new ArrayList<ValidationError>());
        }

        validationErrors.get(file).add(new CheckstyleError(auditEvent));
    }

    @Override
    public Map<File, List<ValidationError>> getValidationErrors() {

        return validationErrors;
    }

    public void writeToJsonFile(final File f) throws IOException {

        final  Gson gson = new GsonBuilder()
//                .registerTypeAdapter(StackTraceElement.class, new StackTraceSerializer())
                .create();

        final Writer w = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(f)), "UTF-8");
        gson.toJson(getValidationErrors(), w);
        w.close();
    }
}
