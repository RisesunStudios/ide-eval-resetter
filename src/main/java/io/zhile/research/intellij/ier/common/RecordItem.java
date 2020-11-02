package io.zhile.research.intellij.ier.common;

public class RecordItem {
    private final RecordType type;
    private final String key;
    private final String value;

    public RecordItem(RecordType type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public RecordItem(RecordType type, String key) {
        this(type, key, null);
    }

    public RecordType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
