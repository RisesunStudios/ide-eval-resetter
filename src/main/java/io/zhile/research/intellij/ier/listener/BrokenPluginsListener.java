package io.zhile.research.intellij.ier.listener;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.helper.BrokenPlugins;
import org.jetbrains.annotations.NotNull;

public class BrokenPluginsListener implements ApplicationActivationListener, Disposable {
    private static BrokenPluginsListener instance = new BrokenPluginsListener();
    private static MessageBusConnection connection;

    protected BrokenPluginsListener() {

    }

    public static BrokenPluginsListener getInstance() {
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
        BrokenPlugins.fix();
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
