package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.component.ResetTimer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AppEventListener implements AppLifecycleListener {
    private static boolean enabled = true;

    public synchronized static void disable() {
        enabled = false;
    }

    public void appFrameCreated(String[] commandLineArgs, @NotNull Ref<Boolean> willOpenProject) {

    }

    @Override
    public void appStarting(@Nullable Project projectFromCommandLine) {

    }

    @Override
    public void projectFrameClosed() {

    }

    @Override
    public void projectOpenFailed() {

    }

    @Override
    public void welcomeScreenDisplayed() {

    }

    @Override
    public void appClosing() {
        if (!enabled || !Resetter.isAutoReset()) {
            return;
        }

        Resetter.reset(Resetter.getEvalRecords());
        ResetTimer.resetLastResetTime();
    }
}
