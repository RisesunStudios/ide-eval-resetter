package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.messages.MessageBusConnection;
import io.zhile.research.intellij.ier.helper.BrokenPlugins;
import io.zhile.research.intellij.ier.helper.CustomProperties;
import io.zhile.research.intellij.ier.helper.CustomRepository;

public class ListenerConnector {
    private static Disposable disposable;

    public static void setup() {
        dispose();

        CustomProperties.fix();
        BrokenPlugins.fix();
        CustomRepository.checkAndAdd(CustomRepository.DEFAULT_HOST);

        Application app = ApplicationManager.getApplication();
        disposable = Disposer.newDisposable();
        Disposer.register(app, disposable);
        MessageBusConnection connection = app.getMessageBus().connect(disposable);
        connection.subscribe(AppLifecycleListener.TOPIC, new AppEventListener());
        connection.subscribe(ApplicationActivationListener.TOPIC, new AppActivationListener());
    }

    public static void dispose() {
        if (null == disposable || Disposer.isDisposed(disposable)) {
            return;
        }

        Disposer.dispose(disposable);
        disposable = null;
    }
}
