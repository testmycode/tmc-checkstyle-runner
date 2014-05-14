package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

public final class CheckstyleError implements ValidationError {

    private final AuditEvent auditEvent;

    public CheckstyleError(final AuditEvent auditEvent) {

        this.auditEvent = auditEvent;
    }

    @Override
    public int getLine() {

        return auditEvent.getLine();
    }

    @Override
    public String getMessage() {

        return auditEvent.getMessage();
    }
}
