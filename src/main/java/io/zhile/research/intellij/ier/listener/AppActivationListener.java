package io.zhile.research.intellij.ier.listener;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.ResetTimeHelper;
import org.jetbrains.annotations.NotNull;

public class AppActivationListener implements ApplicationActivationListener, Disposable {
    private static AppActivationListener instance = new AppActivationListener();
    private static MessageBusConnection connection;

    protected AppActivationListener() {

    }

    public static AppActivationListener getInstance() {
        return instance;
    }

    public synchronized void listen() {
        if (connection != null) {
            return;
        }

        connection = ApplicationManager.getApplication().getMessageBus().connect();
        connection.subscribe(ApplicationActivationListener.TOPIC, this);
    }

    public synchronized void stop() {
        if (connection == null) {
            return;
        }

        connection.disconnect();
        connection = null;
    }

    public void applicationActivated(@NotNull IdeFrame ideFrame) {
        if (!ResetTimeHelper.overResetPeriod()) {
            return;
        }

        stop();

        AnAction action = ActionManager.getInstance().getAction(Constants.RESET_ACTION_ID);
        NotificationType type = NotificationType.INFORMATION;
        String message = "It has been a long time since the last reset!\nWould you like to reset it again?";
        if (Resetter.isAutoReset()) {
            action = ActionManager.getInstance().getAction(Constants.RESTART_ACTION_ID);
            type = NotificationType.WARNING;
        }

        NotificationHelper.show(null, null, null, message, type, action);
    }

    public void applicationDeactivated(@NotNull IdeFrame ideFrame) {
        applicationActivated(ideFrame);
    }

    public void delayedApplicationDeactivated(@NotNull IdeFrame ideFrame) {

    }

    @Override
    public void dispose() {
        stop();
        instance = null;
    }
}
