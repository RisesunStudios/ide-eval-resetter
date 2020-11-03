package io.zhile.research.intellij.ier.helper;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

public class ProjectHelper {
    public static Project getCurrentProject() {
        if (ProjectManager.getInstance().getOpenProjects().length == 0) {
            return null;
        }

        DataContext dataContext = DataManager.getInstance().getDataContextFromFocus().getResultSync();
        return CommonDataKeys.PROJECT.getData(dataContext);
    }

    public static Project getProject(AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            return getCurrentProject();
        }

        return project;
    }
}
