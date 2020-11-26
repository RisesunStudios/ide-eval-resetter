package io.zhile.research.intellij.ier.common;

import com.intellij.ide.util.PropertiesComponent;

public class PropertyRecord implements EvalRecord {
    private final String type = "PROPERTY";
    private final String key;
    private final String value;

    public PropertyRecord(String key) {
        this.key = key;
        this.value = PropertiesComponent.getInstance().getValue(key);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void reset() throws Exception {
        PropertiesComponent.getInstance().unsetValue(key);
    }

    @Override
    public String toString() {
        return type + ": " + key + " = " + (null == value ? "" : value);
    }
}
