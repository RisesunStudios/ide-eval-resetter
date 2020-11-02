package io.zhile.research.intellij.ier.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import io.zhile.research.intellij.ier.component.ResetTimer;
import io.zhile.research.intellij.ier.ui.dialog.MainDialog;
import org.jetbrains.annotations.NotNull;

public class ResetAction extends AnAction implements DumbAware {
    public ResetAction() {
        super("Eval Reset", "Reset my IDE eval information", AllIcons.General.Reset);
        new ResetTimer().start(this);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            MainDialog mainDialog = new MainDialog();
            mainDialog.show();
        } else {
            ToolWindowManager.getInstance(project).getToolWindow("Eval Reset").show(null);
        }
    }
}
