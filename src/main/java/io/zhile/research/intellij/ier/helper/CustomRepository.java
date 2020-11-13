package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public class CustomRepository {
    public static final String DEFAULT_HOST = "https://plugins.zhile.io";

    public static void checkAndAdd(@NotNull String host) {
        List<String> hosts = UpdateSettings.getInstance().getStoredPluginHosts();
        for (String s : hosts) {
            if (s.equalsIgnoreCase(host)) {
                return;
            }
        }

        hosts.add(host);

        Method method = ReflectionHelper.getMethod(UpdateSettings.class, "setThirdPartyPluginsAllowed", boolean.class);
        if (method != null) {
            try {
                method.invoke(UpdateSettings.getInstance(), true);
            } catch (Exception e) {
                NotificationHelper.showError(null, "Enable third party plugins failed!");
            }
        }
    }
}
