package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.BrokenPlugins;
import io.zhile.research.intellij.ier.helper.ResetTimeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AppEventListener implements AppLifecycleListener {
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
        BrokenPlugins.fix();

        if (!Resetter.isAutoReset()) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimeHelper.resetLastResetTime();
    }
}
