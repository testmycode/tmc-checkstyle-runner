package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public final class CheckstyleResultListener implements AuditListener {

    private final CheckstyleResult result = new CheckstyleResult();

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

        result.addError(auditEvent);
    }

    @Override
    public void addException(final AuditEvent auditEvent, final Throwable throwable) {}

    public CheckstyleResult getResults() {

        return result;
    }
}
