package io.zhile.research.intellij.ier.helper;

import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomRepository {
    public static final String DEFAULT_HOST = "https://plugins.zhile.io";

    public static void checkAndAdd(@NotNull String host) {
        List<String> hosts = UpdateSettings.getInstance().getStoredPluginHosts();
        if (hosts.stream().anyMatch(s -> s.equalsIgnoreCase(host))) {
            return;
        }

        hosts.add(host);
    }
}
