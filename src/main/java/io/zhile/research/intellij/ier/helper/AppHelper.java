package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Disposer;
import io.zhile.research.intellij.ier.listener.AppActivationListener;
import io.zhile.research.intellij.ier.listener.AppEventListener;

public class AppHelper {
    public static void restart() {
        Disposer.dispose(AppActivationListener.getInstance());
        Disposer.dispose(AppEventListener.getInstance());

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().restart();
            }
        });
    }
}
