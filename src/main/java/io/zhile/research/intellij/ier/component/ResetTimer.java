package io.zhile.research.intellij.ier.component;

import com.intellij.ide.Prefs;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import io.zhile.research.intellij.ier.action.RestartAction;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.DateTime;
import io.zhile.research.intellij.ier.helper.NotificationHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ResetTimer {
    private static final long RESET_PERIOD = 2160000000L; // 25 days
    private static final String RESET_KEY = Constants.PLUGIN_PREFS_PREFIX + "." + Constants.IDE_NAME_LOWER + "." + Constants.IDE_HASH;

    public static long getLastResetTime() {
        return Prefs.getLong(RESET_KEY, 0L);
    }

    public static void resetLastResetTime() {
        Prefs.putLong(RESET_KEY, System.currentTimeMillis());
        Resetter.syncPrefs();
    }

    public static String getLastResetTimeStr() {
        long lastResetTime = getLastResetTime();

        return lastResetTime > 0 ? DateTime.getStringFromTimestamp(lastResetTime) : "Not yet";
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
            do {
                if (System.currentTimeMillis() - lastResetTime <= RESET_PERIOD) {
                    break;
                }

                AnAction action = resetAction;
                String message = "It has been a long time since the last reset!\nWould you like to reset it again?";

                if (Resetter.isAutoReset()) {
                    Resetter.reset(Resetter.getEvalRecords());
                    ResetTimer.resetLastResetTime();

                    action = new RestartAction();
                    message = "Automatic reset successfully!\nWould like to restart your IDE?";
                }

                Notification notification = NotificationHelper.NOTIFICATION_GROUP.createNotification(Constants.PLUGIN_NAME, null, message, NotificationType.INFORMATION);
                notification.addAction(action);

                Project[] projects = ProjectManager.getInstance().getOpenProjects();
                if (projects.length == 0) {
                    notification.notify(null);
                } else {
                    for (Project project : projects) {
                        notification.notify(project);
                    }
                }
            } while (false);

            new Timer().schedule(new ResetTimerTask(lastResetTime, resetAction), 3600000); // 60 min
        }
    }
}
