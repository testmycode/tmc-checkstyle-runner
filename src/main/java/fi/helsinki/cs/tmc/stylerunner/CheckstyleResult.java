package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckstyleResult {

    private final Map<File, List<AuditEvent>> results;

    public CheckstyleResult() {

        results = new HashMap<File, List<AuditEvent>>();
    }

    public void addError(final AuditEvent auditEvent) {

        final File file = new File(auditEvent.getFileName());

        if (!results.containsKey(file)) {
            results.put(file, new ArrayList<AuditEvent>());
        }

        results.get(file).add(auditEvent);
    }

    public Map<File, List<AuditEvent>> getResults() {

        return results;
    }
}
