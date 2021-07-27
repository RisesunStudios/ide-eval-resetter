package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import io.zhile.research.intellij.ier.helper.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

public class AppInitListener implements AppLifecycleListener, Disposable {
    private static final WeakReference<AnAction> ACTION = new WeakReference<>(ActionManager.getInstance().getAction(Constants.RESET_ACTION_ID));

    protected AppInitListener() {

    }

    public void appFrameCreated(String[] commandLineArgs, @NotNull Ref<Boolean> willOpenProject) {

    }

    public void appStarting(@Nullable Project projectFromCommandLine) {

    }

    public void projectFrameClosed() {

    }

    public void projectOpenFailed() {

    }

    public void welcomeScreenDisplayed() {

    }

    public void appClosing() {

    }

    @Override
    public void dispose() {

    }
}
