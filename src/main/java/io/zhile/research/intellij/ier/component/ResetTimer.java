package io.zhile.research.intellij.ier.component;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.Prefs;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.concurrency.AppExecutorUtil;
import io.zhile.research.intellij.ier.action.RestartAction;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.*;
import io.zhile.research.intellij.ier.listener.AppEventListener;

import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ResetTimer {
    private static final long RESET_PERIOD = 2160000000L; // 25 days
    private static final String RESET_KEY = Constants.PLUGIN_PREFS_PREFIX + "." + Constants.IDE_NAME_LOWER + "." + Constants.IDE_HASH;

    private static ScheduledFuture<?> scheduledFuture;

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

    public synchronized static void start(final WeakReference<AnAction> weakResetAction) {
        if (scheduledFuture != null) {
            return;
        }

        ApplicationManager.getApplication().getMessageBus().connect().subscribe(AppLifecycleListener.TOPIC, new AppEventListener());
        scheduledFuture = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay(() -> {
            if (System.currentTimeMillis() - getLastResetTime() <= RESET_PERIOD) {
                return;
            }

            stop();

            AnAction resetAction = weakResetAction.get();
            if (resetAction == null) {
                return;
            }

            AnAction action = Resetter.isAutoReset() ? new RestartAction() : resetAction;
            String message = "It has been a long time since the last reset!\nWould you like to reset it again?";
            Notification notification = NotificationHelper.NOTIFICATION_GROUP.createNotification(PluginHelper.getPluginName(), null, message, NotificationType.INFORMATION);
            notification.addAction(action);

            notification.notify(ProjectHelper.getCurrentProject());
        }, 3, 600, TimeUnit.SECONDS);
    }

    public synchronized static void stop() {
        if (scheduledFuture == null || scheduledFuture.isCancelled()) {
            return;
        }

        scheduledFuture.cancel(false);
        scheduledFuture = null;
    }
}
