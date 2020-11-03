package io.zhile.research.intellij.ier.action;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import io.zhile.research.intellij.ier.component.ResetTimer;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.helper.ProjectHelper;
import io.zhile.research.intellij.ier.tw.MainToolWindowFactory;
import io.zhile.research.intellij.ier.ui.dialog.MainDialog;
import org.jetbrains.annotations.NotNull;

public class ResetAction extends AnAction implements DumbAware {
    private static final String ACTION_NAME = "Eval Reset";
    private static final DataKey<Notification> NOTIFICATION_KEY = DataKey.create("Notification");

    public ResetAction() {
        super(ACTION_NAME, "Reset my IDE eval information", AllIcons.General.Reset);
        new ResetTimer().start(this);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = ProjectHelper.getProject(e);

        Notification notification = NOTIFICATION_KEY.getData(e.getDataContext());
        if (null != notification) {
            notification.expire();
        }

        if (project == null) {
            MainDialog mainDialog = new MainDialog();
            mainDialog.show();

            return;
        }

        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ACTION_NAME);
        if (null == toolWindow) {
            ToolWindowEP ep = new ToolWindowEP();
            ep.id = ACTION_NAME;
            ep.anchor = ToolWindowAnchor.BOTTOM.toString();
            ep.icon = "AllIcons.General.Reset";
            ep.factoryClass = MainToolWindowFactory.class.getName();
            ep.setPluginDescriptor(Constants.PLUGIN_DESC);
            ToolWindowManagerEx.getInstanceEx(project).initToolWindow(ep);

            toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ACTION_NAME);
        }

        toolWindow.show(null);
    }
}
