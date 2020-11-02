package io.zhile.research.intellij.ier.component;

import com.intellij.ide.Prefs;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.DateTime;
import io.zhile.research.intellij.ier.helper.NotificationHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ResetTimer {
    private static final long RESET_PERIOD = 2160000000L; // 25 days
    private static final String RESET_KEY = "Ide-Eval-Reset." + Constants.IDE_NAME_LOWER + "." + Constants.IDE_HASH;

    public static long getLastResetTime() {
        return Prefs.getLong(RESET_KEY, 0L);
    }

    public static void resetLastResetTime() {
        Prefs.putLong(RESET_KEY, System.currentTimeMillis());
    }

    public static String getLastResetTimeStr() {
        long lastResetTime = getLastResetTime();

        return lastResetTime > 0 ? DateTime.getStringFromTimestamp(lastResetTime) : "not yet";
    }

    public void start(final AnAction resetAction) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new ResetTimerTask(getLastResetTime(), resetAction).run();
            }
        }, 3000);
    }

    protected static class ResetTimerTask extends TimerTask {
        private final long lastResetTime;
        private final AnAction resetAction;

        public ResetTimerTask(long lastResetTime, AnAction resetAction) {
            this.lastResetTime = lastResetTime;
            this.resetAction = resetAction;
        }

        @Override
        public void run() {
            if (System.currentTimeMillis() - lastResetTime > RESET_PERIOD) {
                String message = "It has been a long time since the last reset!\nWould you like to reset it again?";
                Notification notification = NotificationHelper.NOTIFICATION_GROUP.createNotification(Constants.PLUGIN_NAME, null, message, NotificationType.INFORMATION);
                notification.addAction(resetAction);
                notification.notify(null);
            }

            new Timer().schedule(new ResetTimerTask(lastResetTime, resetAction), 3600000); // 60 min
        }
    }
}
