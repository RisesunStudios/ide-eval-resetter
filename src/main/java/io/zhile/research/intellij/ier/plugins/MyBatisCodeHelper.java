package io.zhile.research.intellij.ier.plugins;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import io.zhile.research.intellij.ier.common.PluginRecord;
import io.zhile.research.intellij.ier.helper.ReflectionHelper;

import java.lang.reflect.Method;

public final class MyBatisCodeHelper extends PluginRecord {
    private static final String PLUGIN_NAME = "MyBatisCodeHelperPro (Marketplace Edition)";

    @Override
    public void reset() throws Exception {
        PersistentStateComponent component = (PersistentStateComponent) ApplicationManager.getApplication().getComponent("MyBatisCodeHelper");
        if (null == component) {
            return;
        }

        Object state = component.getState();
        if (null == state) {
            return;
        }

        Method method = ReflectionHelper.getMethod(state.getClass(), "getProfile");
        if (null == method) {
            return;
        }

        Object profile = method.invoke(state);
        method = ReflectionHelper.getMethod(profile.getClass(), "setValid", boolean.class);
        if (null != method) {
            method.invoke(profile, true);
        }

        method = ReflectionHelper.getMethod(profile.getClass(), "setTheUsageCount", String.class);
        if (null != method) {
            method.invoke(profile, "-1");
        }
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
}
