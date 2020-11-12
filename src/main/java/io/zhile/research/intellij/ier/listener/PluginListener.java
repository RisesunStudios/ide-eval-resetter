package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import io.zhile.research.intellij.ier.component.ResetTimer;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.ProjectHelper;
import org.jetbrains.annotations.NotNull;

public class PluginListener implements DynamicPluginListener {
    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        ActionManager.getInstance().getAction("io.zhile.research.intellij.ier.action.ResetAction");

        NotificationHelper.showInfo(null, "Plugin installed successfully! Now enjoy it~");
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        ResetTimer.stop();

        Project project = ProjectHelper.getCurrentProject();
        if (project == null) {
            return;
        }

        ToolWindowManager.getInstance(project).unregisterToolWindow(Constants.ACTION_NAME);
    }
}
