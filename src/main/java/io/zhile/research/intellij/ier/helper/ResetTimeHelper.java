package io.zhile.research.intellij.ier.helper;

import com.intellij.ide.Prefs;
import io.zhile.research.intellij.ier.common.Resetter;

public class ResetTimeHelper {
    public static final long RESET_PERIOD = 2160000000L; // 25 days
    private static final String RESET_KEY = Constants.PLUGIN_PREFS_PREFIX + ".reset_time." + Constants.IDE_NAME + Constants.IDE_BASELINE_VERSION;

    public static long getLastResetTime() {
        return Prefs.getLong(RESET_KEY, 0L);
    }

    public static void resetLastResetTime() {
        Prefs.putLong(RESET_KEY, System.currentTimeMillis());
        Resetter.syncPrefs();
    }

    public static boolean overResetPeriod() {
        return System.currentTimeMillis() - ResetTimeHelper.getLastResetTime() > RESET_PERIOD;
    }

    public static String getLastResetTimeStr() {
        long lastResetTime = getLastResetTime();

        return lastResetTime > 0 ? DateTime.getStringFromTimestamp(lastResetTime) : "Not yet";
    }
}
