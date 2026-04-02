import java.util.prefs.Preferences;

public class UserPreferences {

    public static final String THEME_DARK = "dark";
    public static final String THEME_LIGHT = "light";

    private static final String THEME_KEY = "theme";
    private static final String THEME_DEFAULT = THEME_DARK;

    private final Preferences preferences;

    public UserPreferences() {
        preferences = Preferences.userNodeForPackage(JustGanttApp.class);
    }

    public String getTheme() {
        return preferences.get(THEME_KEY, THEME_DEFAULT);
    }

    public void setTheme(String theme) {
        preferences.put(THEME_KEY, theme);
    }

}
