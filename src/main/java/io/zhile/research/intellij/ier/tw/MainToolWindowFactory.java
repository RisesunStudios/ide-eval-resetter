package io.zhile.research.intellij.ier.tw;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.zhile.research.intellij.ier.helper.Constants;
import io.zhile.research.intellij.ier.ui.form.MainForm;
import org.jetbrains.annotations.NotNull;

public class MainToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Disposable disposable = Disposer.newDisposable();

        MainForm mainForm = new MainForm(disposable);
        Content content = ContentFactory.SERVICE.getInstance().createContent(mainForm.getContent(disposable), "", true);
        content.setDisposer(disposable);
        toolWindow.getContentManager().addContent(content);
    }

    public static void unregisterAll() {
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            ToolWindowManager.getInstance(project).unregisterToolWindow(Constants.ACTION_NAME);
        }
    }
}
