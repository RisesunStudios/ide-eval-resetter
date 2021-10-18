package io.zhile.research.intellij.ier.common;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;

import java.util.List;

abstract public class PluginRecord implements EvalRecord {
    public void test(List<EvalRecord> list) {
        for (IdeaPluginDescriptor descriptor : PluginManager.getPlugins()) {
            if (descriptor.getName().equals(getName())) {
                list.add(this);
                break;
            }
        }
    }

    abstract public String getName();

    @Override
    public String toString() {
        return "PLUGIN: " + getName();
    }
}
