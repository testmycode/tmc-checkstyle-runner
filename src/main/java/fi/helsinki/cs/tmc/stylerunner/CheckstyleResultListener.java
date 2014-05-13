package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CheckstyleResultListener implements AuditListener {

    private final Map<File, List<AuditEvent>> results;

    public CheckstyleResultListener() {

        results = new HashMap<File, List<AuditEvent>>();
    }

    @Override
    public void auditStarted(final AuditEvent auditEvent) {}

    @Override
    public void auditFinished(final AuditEvent auditEvent) {}

    @Override
    public void fileStarted(final AuditEvent auditEvent) {}

    @Override
    public void fileFinished(final AuditEvent auditEvent) {}

    @Override
    public void addError(final AuditEvent auditEvent) {

        final File file = new File(auditEvent.getFileName());

        if (!results.containsKey(file)) {
            results.put(file, new ArrayList<AuditEvent>());
        }

        results.get(file).add(auditEvent);
    }

    @Override
    public void addException(final AuditEvent auditEvent, final Throwable throwable) {}

    public Map<File, List<AuditEvent>> getResults() {

        return results;
    }
}
