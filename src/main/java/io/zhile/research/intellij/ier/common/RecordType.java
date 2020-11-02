package io.zhile.research.intellij.ier.common;

public enum RecordType {
    FILE("FILE"),
    PREFERENCE("PREFERENCE"),
    PROPERTY("PROPERTY");

    private final String value;

    RecordType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
