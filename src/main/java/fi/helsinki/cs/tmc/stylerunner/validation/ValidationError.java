package fi.helsinki.cs.tmc.stylerunner.validation;

public interface ValidationError {

    String getSourceName();
    int getColumn();
    int getLine();
    String getMessage();

}
