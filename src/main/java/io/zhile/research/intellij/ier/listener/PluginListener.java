package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginStateListener;
import com.intellij.ide.plugins.PluginStateManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.NotificationHelper;
import io.zhile.research.intellij.ier.helper.PluginHelper;
import io.zhile.research.intellij.ier.tw.MainToolWindowFactory;
import org.jetbrains.annotations.NotNull;

public class PluginListener implements DynamicPluginListener, PluginStateListener {
    private static final PluginStateListener stateListener = new PluginListener();

    public static void setup() {
        PluginStateManager.addStateListener(stateListener);
    }

    public static void remove() {
        PluginStateManager.removeStateListener(stateListener);
    }

    @Override
    public void install(@NotNull final IdeaPluginDescriptor descriptor) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Resetter.addPluginLicense(descriptor);
            }
        });
    }

    @Override
    public void uninstall(@NotNull IdeaPluginDescriptor descriptor) {

    }

    @Override
    public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
        if (!PluginHelper.myself(pluginDescriptor)) {
            return;
        }

        ActionManager.getInstance().getAction(Constants.RESET_ACTION_ID);

        String link = "https://zhile.io/2020/11/18/jetbrains-eval-reset-da33a93d.html";
        String autoResetTip = "Auto reset switch state: " + (Resetter.isAutoReset() ? "<b>on</b>" : "<b>off<b>");
        String autoLogoutTip = "Auto logout switch state: " + (Resetter.isAutoLogout() ? "<b>on</b>" : "<b>off<b>");
        String content = String.format("Plugin installed successfully!<br>For more information, visit <a href='%s'>this link</a>.<br><br>%s<br>%s", link, autoResetTip, autoLogoutTip);
        NotificationHelper.showInfo(null, content);
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

        ListenerConnector.dispose();
        MainToolWindowFactory.unregisterAll();
    }
}
