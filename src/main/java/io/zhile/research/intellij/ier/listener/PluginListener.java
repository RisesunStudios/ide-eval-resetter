package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.util.Disposer;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.PluginHelper;
import io.zhile.research.intellij.ier.tw.MainToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class PluginListener implements DynamicPluginListener {
    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        if (!PluginHelper.myself(pluginDescriptor)) {
            return;
        }

        ActionManager.getInstance().getAction(Constants.RESET_ACTION_ID);
        NotificationHelper.showInfo(null, "Plugin installed successfully! Now enjoy it~<br>For more information, visit <a href='https://zhile.io/2020/11/18/jetbrains-eval-reset.html'>here</a>.");
    }

    @Override
    public void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
        if (!PluginHelper.myself(pluginDescriptor)) {
            return;
        }

        AnAction optionsGroup = ActionManager.getInstance().getAction("WelcomeScreen.Options");
        if ((optionsGroup instanceof DefaultActionGroup)) {
            ((DefaultActionGroup) optionsGroup).remove(ActionManager.getInstance().getAction(Constants.RESET_ACTION_ID));
        }

        Disposer.dispose(BrokenPluginsListener.getInstance());
        Disposer.dispose(AppActivationListener.getInstance());
        Disposer.dispose(AppEventListener.getInstance());
        MainToolWindowFactory.unregisterAll();
    }
}
