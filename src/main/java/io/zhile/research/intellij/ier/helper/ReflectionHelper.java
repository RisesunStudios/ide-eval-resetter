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

    public static Method getMethod(String className, String methodName, Class<?>... methodParameterTypes) {
        Class<?> klass = getClass(className);
        if (null == klass) {
            return null;
        }

        return getMethod(klass, methodName, methodParameterTypes);
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
