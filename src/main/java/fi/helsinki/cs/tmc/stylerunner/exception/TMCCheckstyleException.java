package fi.helsinki.cs.tmc.stylerunner.exception;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public final class TMCCheckstyleException extends CheckstyleException {

    public TMCCheckstyleException(final String message) {

        super(message);
    }

    public TMCCheckstyleException(final String message, final Throwable cause) {

        super(message, cause);
    }
}
