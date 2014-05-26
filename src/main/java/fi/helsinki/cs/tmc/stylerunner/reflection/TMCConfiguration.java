package fi.helsinki.cs.tmc.stylerunner.reflection;

public final class TMCConfiguration {

    private final boolean enabled;

    public TMCConfiguration(final boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isEnabled() {

        return enabled;
    }
}
