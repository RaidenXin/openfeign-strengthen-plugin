package com.raiden.feign.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 17:19 2021/11/6
 * @Modified By:
 */
public final class FieldUtils {

    public static final void setFieldValue(Object instance, String fieldName,Object value) {
        Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException var5) {
        }
    }

    public static final Object getFieldValue(Object instance, String fieldName) {
        Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
        field.setAccessible(true);

        try {
            return field.get(instance);
        } catch (IllegalAccessException var5) {
            return null;
        }
    }
}
