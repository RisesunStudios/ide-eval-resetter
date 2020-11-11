package io.zhile.research.intellij.ier.common;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.PropertiesComponentImpl;

public class PropertyRecord implements EvalRecord {
    public static final PropertiesComponentImpl PROPS = (PropertiesComponentImpl) PropertiesComponent.getInstance();

    private final String type = "PROPERTY";
    private final String key;
    private final String value;

    public PropertyRecord(String key) {
        this.key = key;
        this.value = PROPS.getValue(key);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void reset() throws Exception {
        PROPS.unsetValue(key);
    }

    @Override
    public String toString() {
        return type + ": " + key + " = " + (null == value ? "" : value);
    }
}
