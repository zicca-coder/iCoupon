package com.zicca.icoupon.framework.toolkit;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * 对象工具类
 *
 * @author zicca
 */
@Slf4j
public final class ObjUtil {

    /**
     * 判断对象及其所有属性是否都不为null
     *
     * @param obj 待检查的对象
     * @return 如果对象不为null且所有属性都不为null则返回true，否则返回false
     */
    public static boolean isNotNull(Object obj) {
        // 如果对象本身为null，返回false
        if (obj == null) {
            return false;
        }

        try {
            // 处理常见的集合和Map类型
            if (obj instanceof Collection) {
                return !((Collection<?>) obj).isEmpty();
            }

            if (obj instanceof Map) {
                return !((Map<?, ?>) obj).isEmpty();
            }

            // 获取对象的所有字段（包括父类字段）
            Class<?> clazz = obj.getClass();
            Field[] fields = getAllFields(clazz);

            // 检查每个字段
            for (Field field : fields) {
                // 跳过静态字段和final字段（常量）
                if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                // 设置可访问私有字段
                field.setAccessible(true);
                Object fieldValue = field.get(obj);

                // 如果有任何字段为null，返回false
                if (fieldValue == null) {
                    return false;
                }
            }

            return true;
        } catch (IllegalAccessException e) {
            log.error("[framework] | 访问对象属性时发生权限异常, class: {}",
                    obj.getClass().getSimpleName(), e);
            return false;
        } catch (Exception e) {
            log.error("[framework] | 检查对象属性时发生未知异常, class: {}",
                    obj.getClass().getSimpleName(), e);
            return false;
        }
    }

    /**
     * 获取类的所有字段（包括父类字段）
     *
     * @param clazz 类
     * @return 所有字段数组
     */
    private static Field[] getAllFields(Class<?> clazz) {
        // 使用StringBuilder风格的集合来收集字段
        java.util.List<Field> fields = new java.util.ArrayList<>();

        // 遍历类层次结构，获取所有字段
        while (clazz != null && clazz != Object.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }

        return fields.toArray(new Field[0]);
    }

    /**
     * 判断对象是否为null或其属性有null
     *
     * @param obj 待检查的对象
     * @return 如果对象为null或其属性有null则返回true，否则返回false
     */
    public static boolean isNull(Object obj) {
        return !isNotNull(obj);
    }


}
