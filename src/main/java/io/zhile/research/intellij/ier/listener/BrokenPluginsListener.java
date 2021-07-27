package io.zhile.research.intellij.ier.listener;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.helper.BrokenPlugins;
import org.jetbrains.annotations.NotNull;

public class BrokenPluginsListener implements ApplicationActivationListener, Disposable {
    private static final Logger LOG = Logger.getInstance(BrokenPluginsListener.class);
    private static BrokenPluginsListener instance;
    private static MessageBusConnection connection;

    protected BrokenPluginsListener() {

    }

    public synchronized static BrokenPluginsListener getInstance() {
        if (instance == null) {
            instance = new BrokenPluginsListener();
        }

        return instance;
    }

    public synchronized void listen() {
        if (connection != null) {
            return;
        }

        try {
            connection = ApplicationManager.getApplication().getMessageBus().connect();
            connection.subscribe(ApplicationActivationListener.TOPIC, this);
        } catch (Exception e) {
            LOG.warn("sub app activation failed.");
        }
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
