package org.jackhuang.watercraft.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
    private ReflectionUtil() {
    }

    public static Field getField(Class<?> clazz, String[] names) {
        for (String name : names) {
            try {
                Field ret = clazz.getDeclaredField(name);
                ret.setAccessible(true);
                return ret;
            } catch (NoSuchFieldException e) {
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    public static Field getField(Class<?> clazz, Class<?> type) {
        Field ret = null;

        for (Field field : clazz.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                if (ret != null)
                    return null;

                field.setAccessible(true);
                ret = field;
            }
        }

        return ret;
    }

    public static Field getFieldRecursive(Class<?> clazz, String fieldName) {
        Field field = null;
        do
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        while ((field == null) && (clazz != null));

        return field;
    }

    public static Object getValue(Object object, Class<?> type) {
        Field field = getField(object.getClass(), type);
        if (field == null)
            return null;
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getValueRecursive(Object object, String fieldName) throws NoSuchFieldException {
        Field field = getFieldRecursive(object.getClass(), fieldName);
        if (field == null)
            throw new NoSuchFieldException(fieldName);

        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(Class<?> clazz, String[] names, Class<?>[] parameterTypes) {
        for (String name : names) {
            try {
                Method ret = clazz.getDeclaredMethod(name, parameterTypes);
                ret.setAccessible(true);
                return ret;
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}