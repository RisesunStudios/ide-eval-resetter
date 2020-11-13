package io.zhile.research.intellij.ier.component;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.Prefs;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.action.ResetAction;
import io.zhile.research.intellij.ier.action.RestartAction;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.DateTime;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.PluginHelper;
import io.zhile.research.intellij.ier.listener.AppEventListener;

import java.lang.ref.WeakReference;

public class ResetTimer {
    private static final long RESET_PERIOD = 2160000000L; // 25 days
    private static final String RESET_KEY = Constants.PLUGIN_PREFS_PREFIX + "." + Constants.IDE_NAME_LOWER + "." + Constants.IDE_HASH;

    private static MessageBusConnection connection;

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

    public synchronized static void start(final WeakReference<ResetAction> weakResetAction) {
        if (connection != null) {
            return;
        }

        ApplicationManager.getApplication().getMessageBus().connect().subscribe(AppLifecycleListener.TOPIC, new AppEventListener());

        connection = ApplicationManager.getApplication().getMessageBus().connect();
        connection.subscribe(ApplicationActivationListener.TOPIC, new ApplicationActivationListener() {
            public void applicationActivated(IdeFrame ideFrame) {
                if (System.currentTimeMillis() - getLastResetTime() <= RESET_PERIOD) {
                    return;
                }

                stop();

                AnAction resetAction = weakResetAction.get();
                if (resetAction == null) {
                    return;
                }

                AnAction action = resetAction;
                NotificationType type = NotificationType.INFORMATION;
                String message = "It has been a long time since the last reset!\nWould you like to reset it again?";
                if (Resetter.isAutoReset()) {
                    action = new RestartAction();
                    type = NotificationType.WARNING;
                }

                Notification notification = NotificationHelper.NOTIFICATION_GROUP.createNotification(PluginHelper.getPluginName(), null, message, type);
                notification.addAction(action);
                notification.notify(null);
            }

            public void applicationDeactivated(IdeFrame ideFrame) {
                applicationActivated(ideFrame);
            }

            public void delayedApplicationDeactivated(IdeFrame ideFrame) {

            }
        });
    }

    public synchronized static void stop() {
        if (connection == null) {
            return;
        }

        connection.disconnect();
        connection = null;
    }
}
