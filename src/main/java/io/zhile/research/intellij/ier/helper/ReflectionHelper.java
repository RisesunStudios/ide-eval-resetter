package io.zhile.research.intellij.ier.helper;

import java.lang.reflect.Method;

public class ReflectionHelper {
    public static Method getMethod(Class<?> klass, String methodName, Class<?>... methodParameterTypes) {
        try {
            return klass.getMethod(methodName, methodParameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
