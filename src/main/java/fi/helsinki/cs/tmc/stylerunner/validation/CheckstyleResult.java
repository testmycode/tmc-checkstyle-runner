package fi.helsinki.cs.tmc.stylerunner.validation;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

import java.io.File;
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
}
