package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.BrokenPlugins;
import io.zhile.research.intellij.ier.helper.ResetTimeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AppEventListener implements AppLifecycleListener, Disposable {
    private static AppEventListener instance = new AppEventListener();
    private static MessageBusConnection connection;

    protected AppEventListener() {

    }

    public static AppEventListener getInstance() {
        return instance;
    }

    public synchronized void listen() {
        if (connection != null) {
            return;
        }

        connection = ApplicationManager.getApplication().getMessageBus().connect();
        connection.subscribe(AppLifecycleListener.TOPIC, this);
    }

    public synchronized void stop() {
        if (connection == null) {
            return;
        }

        connection.disconnect();
        connection = null;
    }

    public void appFrameCreated(String[] commandLineArgs, @NotNull Ref<Boolean> willOpenProject) {

    }

    public void appStarting(@Nullable Project projectFromCommandLine) {

    }

    public void projectFrameClosed() {

    }

    public void projectOpenFailed() {

    }

    public void welcomeScreenDisplayed() {

    }

    public void appClosing() {
        BrokenPlugins.fix();

        if (!Resetter.isAutoReset()) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimeHelper.resetLastResetTime();
    }

    @Override
    public void dispose() {
        stop();
        instance = null;
    }
}
