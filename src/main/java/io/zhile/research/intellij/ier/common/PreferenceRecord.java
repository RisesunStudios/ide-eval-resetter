package io.zhile.research.intellij.ier.common;

import com.intellij.ide.Prefs;

import java.util.prefs.Preferences;

public class PreferenceRecord implements EvalRecord {
    private static final String DEFAULT_VALUE = null;

    private final String type = "PREFERENCE";
    private final String key;
    private final String value;
    private final boolean isRaw;

    public PreferenceRecord(String key) {
        this(key, false);
    }

    public PreferenceRecord(String key, boolean isRaw) {
        this.key = key;
        this.isRaw = isRaw;
        this.value = isRaw ? Preferences.userRoot().get(key, DEFAULT_VALUE) : Prefs.get(key, DEFAULT_VALUE);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void reset() throws Exception {
        if (isRaw) {
            Preferences.userRoot().remove(key);
        } else {
            Prefs.remove(key);
        }

        Resetter.syncPrefs();
    }

    @Override
    public String toString() {
        return type + ": " + key + " = " + (null == value ? "" : value);
    }
}
