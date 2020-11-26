package io.zhile.research.intellij.ier.helper;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;

public class PluginHelper {
    public static PluginId getPluginId() {
        return PluginId.getId(Constants.PLUGIN_ID_STR);
    }

    public static IdeaPluginDescriptor getPluginDescriptor() {
        return PluginManager.getPlugin(getPluginId());
    }

    public static String getPluginName() {
        IdeaPluginDescriptor pluginDescriptor = getPluginDescriptor();
        return pluginDescriptor == null ? "UNKNOWN" : pluginDescriptor.getName();
    }

    public static String getPluginVersion() {
        IdeaPluginDescriptor pluginDescriptor = getPluginDescriptor();
        return pluginDescriptor == null ? "UNKNOWN" : pluginDescriptor.getVersion();
    }

    public static boolean myself(IdeaPluginDescriptor pluginDescriptor) {
        return Constants.PLUGIN_ID_STR.equals(pluginDescriptor.getPluginId().getIdString());
    }
}
