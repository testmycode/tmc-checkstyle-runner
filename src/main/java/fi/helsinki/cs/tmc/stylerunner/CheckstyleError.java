package fi.helsinki.cs.tmc.stylerunner;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;

public class CheckstyleError implements ValidationError {

    private final AuditEvent auditEvent;

    public CheckstyleError(AuditEvent auditEvent) {

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
