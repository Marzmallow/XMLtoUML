package uml.controllers.boxEditor.layoutEditor;

import java.util.prefs.Preferences;

/**
 * @author Dries Marzougui
 *
 * Saves the user-defined layout preferences.
 */
public class BoxPreferences {
    private Preferences prefs;

    public BoxPreferences() {
    // This will define a node in which the preferences can be stored
        prefs = Preferences.userRoot().node(this.getClass().getName());

        // Defaults
        prefs.put("boxBackgroundColor", "antiquewhite");
        prefs.put("boxBorderColor", "black");
        prefs.put("boxTextColor", "black");
        prefs.put("boxTextFontFamily", "Arial");
    }

    public void setPreference(String preferenceName, String preferenceValue) {
        prefs.put(preferenceName, preferenceValue);
    }

    public String getPreference(String preferenceName, String defaultValue) {
        return prefs.get(preferenceName, defaultValue);
    }

    public Preferences getPrefs() {
        return prefs;
    }
}
