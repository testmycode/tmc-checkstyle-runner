package fi.helsinki.cs.tmc.stylerunner.configuration;

public final class TMCConfiguration {

    private final boolean enabled;
    private final String rule;

    public TMCConfiguration() {

        // Default configuration
        enabled = true;
        rule = "default-checkstyle.xml";
    }

    public boolean isEnabled() {

        return enabled;
    }

    public String getRule() {

        return rule;
    }

    // TODO: Return InputSource of checkstyle.xml
}
