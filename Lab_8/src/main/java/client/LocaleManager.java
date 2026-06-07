package client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LocaleManager {

    private static final ObjectProperty<Locale> localeProperty =
            new SimpleObjectProperty<>(new Locale("ru"));

    private static ResourceBundle bundle;

    static {
        reload();
    }

    public static void setLocale(Locale locale) {
        localeProperty.set(locale);
        reload();
    }

    private static void reload() {
        try {
            bundle = ResourceBundle.getBundle("i18n.messages", localeProperty.get());
        } catch (MissingResourceException e) {
            bundle = ResourceBundle.getBundle("i18n.messages", new Locale("ru"));
        }
    }

    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "[" + key + "]";
        }
    }

    public static Locale getLocale() {
        return localeProperty.get();
    }

    public static ObjectProperty<Locale> localeProperty() {
        return localeProperty;
    }

    public static NumberFormat getNumberFormat() {
        return NumberFormat.getNumberInstance(localeProperty.get());
    }

    public static DateFormat getDateTimeFormat() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, localeProperty.get());
    }

    public static DateFormat getDateFormat() {
        return DateFormat.getDateInstance(DateFormat.SHORT, localeProperty.get());
    }

    public static List<Locale> getSupportedLocales() {
        return Arrays.asList(
                new Locale("ru"),
                new Locale("fi"),
                new Locale("lv"),
                new Locale("es", "EC")
        );
    }

    public static String getDisplayName(Locale locale) {
        String tag = locale.toLanguageTag().replace("-", "_");
        try {
            return bundle.getString("lang." + tag);
        } catch (MissingResourceException e) {
            return locale.getDisplayName(locale);
        }
    }
}
