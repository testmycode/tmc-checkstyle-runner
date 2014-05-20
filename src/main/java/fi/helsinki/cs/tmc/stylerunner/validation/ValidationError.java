package fi.helsinki.cs.tmc.stylerunner.validation;

public interface ValidationError {

    int getColumn();
    int getLine();
    String getMessage();
    String getSourceName();

}
