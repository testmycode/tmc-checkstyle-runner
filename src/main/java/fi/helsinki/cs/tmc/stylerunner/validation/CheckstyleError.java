package fi.helsinki.cs.tmc.stylerunner.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.LocalizedMessage;

public final class CheckstyleError implements ValidationError {

    private final AuditEvent auditEvent;

    @JsonCreator
    public CheckstyleError(@JsonProperty("line") final int lineNumber,
                             @JsonProperty("column") final int columnNumber,
                             @JsonProperty("message") final String message,
                             @JsonProperty("sourceName") final String sourceName) {

        this(new AuditEvent(sourceName,
                            null,
                            new LocalizedMessage(lineNumber,
                                                 columnNumber,
                                                 null,
                                                 null,
                                                 null,
                                                 null,
                                                 String.class,
                                                 message)));
    }

    public CheckstyleError(final AuditEvent auditEvent) {

        this.auditEvent = auditEvent;
    }

    @Override
    public int getColumn() {

        return auditEvent.getColumn();
    }

    @Override
    public int getLine() {

        return auditEvent.getLine();
    }

    @Override
    public String getMessage() {

        return auditEvent.getMessage();
    }

    @Override
    public String getSourceName() {

        return auditEvent.getSourceName();
    }

}
