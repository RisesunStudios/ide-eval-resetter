package io.zhile.research.intellij.ier.common;

import com.intellij.ide.Prefs;

import java.util.prefs.Preferences;

public class PreferenceRecord implements EvalRecord {
    private static final String DEFAULT_VALUE = null;

    private final String type = "PREFERENCE";
    private final String key;
    private final String value;
    private final boolean isRaw;
    private final KeepCondition keepCondition;

    public PreferenceRecord(String key) {
        this(key, false, null);
    }

    public PreferenceRecord(String key, boolean isRaw) {
        this(key, isRaw, null);
    }

    public PreferenceRecord(String key, boolean isRaw, KeepCondition keepCondition) {
        this.key = key;
        this.isRaw = isRaw;
        this.keepCondition = keepCondition;
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
        if (null != keepCondition && keepCondition.needKeep()) {
            return;
        }

        if (isRaw) {
            Preferences.userRoot().remove(key);
        } else {
            Prefs.remove(key);
        }

        Resetter.syncPrefs();
    }

    @Override
    public String toString() {
        String v = null == value ? "" : value;

        return type + ": " + key + " = " + v.substring(0, Math.min(36, v.length()));
    }
}
