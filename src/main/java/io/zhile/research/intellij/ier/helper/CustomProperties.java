package io.zhile.research.intellij.ier.helper;

public class CustomProperties {
    public static void fix() {
        String key = "idea.ignore.disabled.plugins";
        System.clearProperty(key);
    }
}
