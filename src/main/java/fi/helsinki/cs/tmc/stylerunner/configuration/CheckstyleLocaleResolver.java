package fi.helsinki.cs.tmc.stylerunner.configuration;

import java.util.Locale;

public final class CheckstyleLocaleResolver {

    private CheckstyleLocaleResolver() {}

    public static Locale get(final Locale locale) {

        if (locale.equals(Locale.ENGLISH)) {
            return Locale.ROOT;
        }

        if (locale.equals(Locale.forLanguageTag("fi"))) {
            return locale;
        }

        return Locale.ROOT;
    }

}
