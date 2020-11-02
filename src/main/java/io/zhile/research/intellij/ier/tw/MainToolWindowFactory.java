package io.zhile.research.intellij.ier.tw;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.zhile.research.intellij.ier.ui.form.MainForm;
import org.jetbrains.annotations.NotNull;

public class MainToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MainForm mainForm = new MainForm(null);
        Content content = ContentFactory.SERVICE.getInstance().createContent(mainForm.getContent(), "", true);
        toolWindow.getContentManager().addContent(content);
    }
}
