package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.BuildNumber;

public class AppHelper {
    public static void restart() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().restart();
            }
        });
    }

    public static String getProductName() {
        String productName = Constants.IDE_NAME;

        if ("IDEA".equals(productName)) {
            return productName.toLowerCase();
        }

        return productName;
    }

    public static BuildNumber getBuildNumber() {
        return ApplicationInfo.getInstance().getBuild();
    }
}
