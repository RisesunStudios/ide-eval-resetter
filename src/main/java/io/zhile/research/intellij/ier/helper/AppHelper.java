package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.ApplicationManager;

public class AppHelper {
    public static void restart() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().restart();
            }
        });
    }
}
