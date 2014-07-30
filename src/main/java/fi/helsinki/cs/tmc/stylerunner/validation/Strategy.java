package fi.helsinki.cs.tmc.stylerunner.validation;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Strategy {

    FAIL,
    WARN,
    DISABLED;

    @JsonCreator
    public static Strategy fromValue(final String value) {

        return Strategy.valueOf(Strategy.class, value.toUpperCase());
    }
}
