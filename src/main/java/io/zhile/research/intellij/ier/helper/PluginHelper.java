package io.zhile.research.intellij.ier.helper;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

public class PluginHelper {
    private static final IdeaPluginDescriptor PLUGIN_DESCRIPTOR = PluginManager.getPlugin(getPluginId());

    public static PluginId getPluginId() {
        return PluginId.getId(Constants.PLUGIN_ID_STR);
    }

    public static IdeaPluginDescriptor getPluginDescriptor() {
        return PLUGIN_DESCRIPTOR;
    }

    public static String getPluginName() {
        return PLUGIN_DESCRIPTOR == null ? "UNKNOWN" : PLUGIN_DESCRIPTOR.getName();
    }

    public static String getPluginVersion() {
        return PLUGIN_DESCRIPTOR == null ? "UNKNOWN" : PLUGIN_DESCRIPTOR.getVersion();
    }
}
