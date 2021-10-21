package io.zhile.research.intellij.ier.listener;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginInstaller;
import com.intellij.ide.plugins.PluginStateListener;
import com.intellij.openapi.application.ApplicationManager;
import io.zhile.research.intellij.ier.common.Resetter;
import io.zhile.research.intellij.ier.helper.ReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class PluginInstallListener implements PluginStateListener {
    private static final PluginStateListener stateListener = new PluginInstallListener();

    private static void reflectionCall(String methodName) throws Exception {
        Method[] methods = new Method[]{
                ReflectionHelper.getMethod(PluginInstaller.class, methodName, PluginStateListener.class),
                ReflectionHelper.getMethod("com.intellij.ide.plugins.PluginStateManager", methodName, PluginStateListener.class),
        };

        for (Method method : methods) {
            if (null == method) {
                continue;
            }

            method.invoke(null, stateListener);
            return;
        }
    }

    public static void setup() throws Exception {
        reflectionCall("addStateListener");
    }

    public static void remove() throws Exception {
        reflectionCall("removeStateListener");
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
}
