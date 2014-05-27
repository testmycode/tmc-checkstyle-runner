package fi.helsinki.cs.tmc.stylerunner.configuration;

public final class TMCConfiguration {

    private final boolean enabled;
    private final String rule;

    public TMCConfiguration() {

        enabled = true;
        rule = "default-checkstyle.xml";
    }

    public boolean isEnabled() {

        return enabled;
    }

    public String getRule() {

        return rule;
    }
}
