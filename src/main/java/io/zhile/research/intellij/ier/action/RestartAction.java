package io.zhile.research.intellij.ier.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;

public class RestartAction extends AnAction implements DumbAware {
    public RestartAction() {
        super("Restart IDE", "Restart my IDE", AllIcons.Actions.Restart);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().restart();
            }
        });
    }
}
