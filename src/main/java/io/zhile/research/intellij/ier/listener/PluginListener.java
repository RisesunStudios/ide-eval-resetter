package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.actionSystem.ActionManager;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.tw.MainToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class PluginListener implements DynamicPluginListener {
    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        ActionManager.getInstance().getAction("io.zhile.research.intellij.ier.action.ResetAction");

        NotificationHelper.showInfo(null, "Plugin installed successfully! Now enjoy it~");
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        MainToolWindowFactory.unregisterAll();
    }
}
