package com.baoge.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeanUtils implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);
    private static final long serialVersionUID = 1L;

    public BeanUtils() {
    }

    public static String getBeanFullName(Class<?> clazz) {
        return clazz.getName();
    }

    public static String getBeanName(Class<?> clazz) {
        String fullName = getBeanFullName(clazz);
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }

    public static String getBeanName(String fullClassName) {
        return StringUtils.isNotBlank(fullClassName) ? fullClassName.substring(fullClassName.lastIndexOf(".") + 1) : fullClassName;
    }

    public static <T> Map<String, Object> getBeanQueryMap(T t) {
        if (t != null) {
            if (t instanceof Map) {
                return (Map) t;
            } else {
                Map<String, Object> result = new LinkedHashMap(16);
                Field[] fields = getFieldArrays(t.getClass());
                Field[] var3 = fields;
                int var4 = fields.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    Field field = var3[var5];
                    if (!Modifier.isStatic(field.getModifiers())) {
                        String fieldName = field.getName();

                        try {
                            Method getter = getterMethod(fieldName, t.getClass());
                            Object value = getter.invoke(t);
                            if (value != null) {
                                result.put(fieldName, value);
                            }
                        } catch (IntrospectionException var10) {
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException |
                                 SecurityException var11) {
                            log.error("========[BeanUtils-getBeanQueryMap-Error:{}]========", var11);
                        }
                    }
                }

                return result;
            }
        } else {
            return null;
        }
    }

    public static <T> List<String> getFieldNameList(T t) {
        return t != null ? getFieldNameList(t.getClass()) : null;
    }

    public static <T> List<String> getFieldNameList(Class<T> entityClass) {
        if (entityClass != null) {
            List<String> result = new ArrayList();
            Field[] fields = getFieldArrays(entityClass);
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (!Modifier.isStatic(field.getModifiers())) {
                    result.add(field.getName());
                }
            }

            return result;
        } else {
            return null;
        }
    }

    public static <T> List<String> getBeanAttributeList(T t) {
        if (t != null) {
            List<String> result = new ArrayList();
            Field[] fields = getFieldArrays(t.getClass());
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (!isStatic) {
                    String fieldName = field.getName();

                    try {
                        Method getter = getterMethod(fieldName, t.getClass());
                        Object value = getter.invoke(t);
                        if (value != null) {
                            result.add(value.toString());
                        }
                    } catch (IntrospectionException var11) {
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException |
                             SecurityException var12) {
                        log.error("========[BeanUtils-getBeanAttributeList-Error:{}]========", var12);
                    }
                }
            }

            return result;
        } else {
            return null;
        }
    }

    public static <T> Object getFieldValue(T t, String fieldName) {
        if (t != null && StringUtils.isNotEmpty(fieldName)) {
            try {
                Method getter = getterMethod(fieldName, t.getClass());
                return getter.invoke(t);
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
                     IntrospectionException var3) {
                log.error("========[BeanUtils-getFieldValue-Error:{}]========", var3);
            }
        }

        return null;
    }

    public static <A extends Annotation> A getAnnotation(Class<?> entityClass, Class<A> annotationClass) {
        A a = null;
        if (entityClass != null && annotationClass != null) {
            a = entityClass.getAnnotation(annotationClass);
        }

        return a;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationClass) {
        A a = null;
        if (method != null && annotationClass != null) {
            a = method.getAnnotation(annotationClass);
        }

        return a;
    }

    public static <A extends Annotation> boolean hasAnnation(Class<?> entityClass, Class<A> annotationClass) {
        if (entityClass != null && annotationClass != null) {
            A a = entityClass.getAnnotation(annotationClass);
            return a != null;
        } else {
            return false;
        }
    }

    public static <T> Field getFieldByName(Class<?> entityClass, String fieldName) {
        Field field = null;
        if (entityClass != null && !entityClass.equals(Object.class) && StringUtils.isNotEmpty(fieldName)) {
            try {
                field = entityClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                field = getFieldByName(entityClass.getSuperclass(), fieldName);
            }
        }

        return field;
    }


    private static void createInitFieldMap(Map<String, Object> fieldMap, String fieldName, Object value) {
        String regex = "(\\[\\d+\\])";
        String fieldKey = fieldName.substring(0, fieldName.indexOf("."));
        String fieldValueKey = fieldName.substring(fieldName.indexOf(".") + 1);
        String arrayIndex = getPatternValue(regex, fieldKey);
        Map<String, Object> valueMap = (Map) fieldMap.get(fieldKey.replaceAll(regex, ""));
        if (valueMap == null) {
            valueMap = new LinkedHashMap(16);
        }

        if (arrayIndex.length() > 0) {
            fieldKey = fieldKey.replaceAll(regex, "");
            String indexKey = arrayIndex.replaceAll("[\\[\\]]", "");
            Map<String, Object> arrayValueMap = (Map) ((Map) valueMap).get(indexKey);
            if (arrayValueMap == null) {
                arrayValueMap = new LinkedHashMap(16);
            }

            ((Map) arrayValueMap).put(fieldValueKey, value);
            ((Map) valueMap).put(indexKey, arrayValueMap);
        } else {
            ((Map) valueMap).put(fieldValueKey, value);
        }

        fieldMap.put(fieldKey, valueMap);
    }

    public static <T> T setFieldValue(T t, Field field, Object value) {
        if (t != null && field != null) {
            String fieldName = field.getName();

            try {
                Method setter = setterMethod(fieldName, t.getClass());
                if (value != null) {
                    invokeValue(setter, t, field, value);
                }
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException |
                     IntrospectionException var5) {
                log.info("========[BeanUtils-setFieldValue-Error:{}-field:{}]========", var5, fieldName);
            }
        }

        return t;
    }

    public static <T> T newInstance(Class<T> entityClass) {
        if (entityClass != null) {
            try {
                return entityClass.newInstance();
            } catch (IllegalAccessException | InstantiationException var2) {
                log.error("========[BeanUtils-newInstance-Error:{}]========", var2);
            }
        }

        return null;
    }

    public static List<Map<String, Object>> getFieldDetails(Object t) {
        List<Map<String, Object>> result = null;
        if (t != null) {
            result = new ArrayList();
            Field[] fields = t.getClass().getDeclaredFields();
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];

                try {
                    Map<String, Object> map = new LinkedHashMap(16);
                    String fieldName = field.getName();
                    Method getter = getterMethod(fieldName, t.getClass());
                    map.put("name", fieldName);
                    map.put("field", field);
                    map.put("typeName", field.getType().getName());
                    map.put("type", field.getType());
                    map.put("value", getter.invoke(t));
                    result.add(map);
                } catch (IntrospectionException var10) {
                } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var11) {
                    log.error("========[BeanUtils-getFieldDetails-Error:{}]========", var11);
                }
            }
        }

        return result;
    }

    public static <A extends Annotation> Field getAnnotationField(A annotation, String fieldName) {
        Field field = null;
        if (annotation != null && StringUtils.isNotBlank(fieldName)) {
            try {
                field = annotation.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                log.info("=============getAnnotationField-no-field:[{}]=============", fieldName);
            }
        }

        return field;
    }

    public static <T> Field getField(T t, String fieldName) {
        Field field = null;
        if (t != null && StringUtils.isNotBlank(fieldName)) {
            try {
                field = t.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                log.info("=============getAnnotationField-no-field:[{}]=============", fieldName);
            }
        }

        return field;
    }

    public static <T> Object getValue(T t, String fieldName) {
        if (t != null) {
            if (t instanceof Map) {
                return ((Map) t).get(fieldName);
            }

            try {
                Method getter = getterMethod(fieldName, t.getClass());
                return getter.invoke(t);
            } catch (IntrospectionException | IllegalArgumentException | InvocationTargetException |
                     IllegalAccessException | SecurityException var3) {
                log.error("========[BeanUtils-getValue-Error:{}]========", var3);
            }
        }

        return null;
    }

    public static <T> List<Map<Class<?>, Object>> getFieldValues(T t) {
        List<Map<Class<?>, Object>> result = null;
        if (t != null) {
            result = new ArrayList();
            Field[] fields = t.getClass().getDeclaredFields();
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];

                try {
                    Map<Class<?>, Object> map = new LinkedHashMap(16);
                    String fieldName = field.getName();
                    Method getter = getterMethod(fieldName, t.getClass());
                    map.put(field.getType(), getter.invoke(t));
                    result.add(map);
                } catch (IntrospectionException var10) {
                } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var11) {
                    log.error("========[BeanUtils-getFieldValues-Error:{}]========", var11);
                }
            }
        }

        return result;
    }

    public static String getRealFieldName(Class<?> clazz, String... fieldNames) {
        if (clazz != null) {
            String[] var2 = fieldNames;
            int var3 = fieldNames.length;
            int var4 = 0;

            while (var4 < var3) {
                String name = var2[var4];

                try {
                    clazz.getDeclaredField(name);
                    return name;
                } catch (NoSuchFieldException var7) {
                    log.error("========[BeanUtils-getRealFieldName-Error:{}]========", var7);
                    ++var4;
                }
            }
        }

        return null;
    }

    public static Field getRealField(Class<?> clazz, String... fieldNames) {
        Field field = null;
        if (clazz != null) {
            String[] var3 = fieldNames;
            int var4 = fieldNames.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String name = var3[var5];

                try {
                    field = clazz.getDeclaredField(name);
                    if (field != null) {
                        return field;
                    }
                } catch (NoSuchFieldException var8) {
                    log.error("========[BeanUtils-getRealField-Error:{}]========", var8);
                }
            }
        }

        return field;
    }

    public static boolean checkBaseType(Class<?> entityClass) {
        if (entityClass == null) {
            return false;
        } else {
            boolean result = entityClass.equals(String.class) || entityClass.equals(Date.class) || entityClass.equals(Integer.class) || entityClass.equals(Integer.TYPE) || "java.math.BigInteger".equals(entityClass.getName()) || "java.io.Serializable".equals(entityClass.getName()) || entityClass.equals(Long.class) || entityClass.equals(Long.TYPE) || entityClass.equals(Short.class) || entityClass.equals(Short.TYPE) || entityClass.equals(Double.class) || entityClass.equals(Double.TYPE) || entityClass.equals(BigDecimal.class) || entityClass.equals(Number.class) || entityClass.equals(Float.class) || entityClass.equals(Float.TYPE) || entityClass.equals(Boolean.class) || entityClass.equals(Boolean.TYPE) || entityClass.equals(Character.class) || entityClass.equals(Character.TYPE) || entityClass.isArray() && checkBaseType(entityClass.getComponentType());
            return result;
        }
    }


    public static <T> Method getterMethod(String fieldName, Class<T> entityClass) throws IntrospectionException {
        Method getter = null;

        try {
            getter = getPropertyDescriptor(fieldName, entityClass).getReadMethod();
        } catch (IntrospectionException var5) {
            log.info("============entityClass:{}--not-Found-method:{}============", entityClass, fieldName);
            Class<?> supperClass = entityClass.getSuperclass();
            if (entityClass != null && !entityClass.equals(Object.class) && !supperClass.equals(Object.class)) {
                getter = getterMethod(fieldName, supperClass);
            }
        }

        if (getter == null) {
            throw new IntrospectionException("no-method-getter:[" + fieldName + "]");
        } else {
            return getter;
        }
    }

    public static <T> Method setterMethod(String fieldName, Class<T> entityClass) throws IntrospectionException {
        return getPropertyDescriptor(fieldName, entityClass).getWriteMethod();
    }

    private static <T> void invokeValue(Method setter, T t, Field field, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (field.getType().equals(String.class) && value instanceof byte[]) {
            setter.invoke(t, new String((byte[]) ((byte[]) value)), Charset.defaultCharset());
        } else if (field.getType().equals(String.class) && value instanceof Byte[]) {
            byte[] b = new byte[((Byte[]) ((Byte[]) value)).length];
            int index = 0;
            Byte[] var6 = (Byte[]) ((Byte[]) value);
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                Byte bb = var6[var8];
                b[index++] = bb;
            }

            setter.invoke(t, new String(b, Charset.defaultCharset()));
        } else if (field.getType().equals(String.class)) {
            setter.invoke(t, value.toString());
        } else if (!field.getType().equals(Integer.TYPE) && !field.getType().equals(Integer.class)) {
            if (!field.getType().equals(Long.TYPE) && !field.getType().equals(Long.class) && !field.getType().equals(Serializable.class)) {
                if (!field.getType().equals(Short.TYPE) && !field.getType().equals(Short.class)) {
                    BigDecimal b;
                    if (!field.getType().equals(Double.TYPE) && !field.getType().equals(Double.class)) {
                        if (!field.getType().equals(Float.TYPE) && !field.getType().equals(Float.class)) {
                            if (!field.getType().equals(Boolean.TYPE) && !field.getType().equals(Boolean.class)) {
                                if (!field.getType().equals(Character.class) && !field.getType().equals(Character.TYPE)) {
                                    if (field.getType().equals(BigDecimal.class)) {
                                        b = new BigDecimal(Double.parseDouble(value.toString()));
                                        setter.invoke(t, b.setScale(4, 4));
                                    } else if (field.getType().equals(Date.class)) {
                                        if (value instanceof String) {
                                            setter.invoke(t, new Date(Long.parseLong(value.toString())));
                                        } else if (value.getClass().equals(Long.TYPE)) {
                                            setter.invoke(t, new Date((Long) value));
                                        } else if (value.getClass().equals(Long.class)) {
                                            setter.invoke(t, new Date((Long) value));
                                        } else {
                                            setter.invoke(t, value);
                                        }
                                    } else {
                                        setter.invoke(t, value);
                                    }
                                } else {
                                    setter.invoke(t, value.toString().toCharArray()[0]);
                                }
                            } else {
                                setter.invoke(t, Boolean.parseBoolean(value.toString()));
                            }
                        } else {
                            setter.invoke(t, Float.parseFloat(value.toString()));
                        }
                    } else {
                        b = new BigDecimal(Double.parseDouble(value.toString()));
                        b = b.setScale(4, 4);
                        setter.invoke(t, b.doubleValue());
                    }
                } else {
                    setter.invoke(t, Short.parseShort(value.toString()));
                }
            } else {
                setter.invoke(t, Long.parseLong(value.toString()));
            }
        } else {
            setter.invoke(t, Integer.parseInt(value.toString()));
        }

    }

    public static Class<?> getListGenericClass(Class<?> entityClass, String listName) {
        if (entityClass != null && StringUtils.isNoneBlank(new CharSequence[]{listName})) {
            try {
                Field listField = entityClass.getDeclaredField(listName);
                if (listField != null && listField.getType() == List.class) {
                    return getGenericClass(listField.getGenericType());
                }
            } catch (SecurityException | NoSuchFieldException var3) {
                log.error("==========getListGenericClass-field-{}-not-found:{}==========", listName, var3);
            }
        }

        return null;
    }

    public static <T> Class<?> getListGenericClass(List<T> list) {
        Object first;
        return list != null && !list.isEmpty() && (first = list.get(0)) != null ? first.getClass() : null;
    }

    public static <K> Class<K> getGenericClass(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            System.out.println(Arrays.toString(pt.getActualTypeArguments()));

            try {
                return (Class) pt.getActualTypeArguments()[0];
            } catch (ClassCastException var3) {
                log.error("======getGenericClass-error:{}======", var3);
            }
        }

        return null;
    }

    public static <T> List<Field> getFields(Class<?> entityClass) {
        return Arrays.asList(entityClass.getDeclaredFields());
    }

    private static <T> PropertyDescriptor getPropertyDescriptor(String fieldName, Class<T> entityClass) throws IntrospectionException {
        return new PropertyDescriptor(fieldName, entityClass);
    }

    private static String getPatternValue(String regex, String src) {
        StringBuilder builder = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);
        int index = 0;

        while (matcher.find()) {
            builder.append(matcher.group(index++));
        }

        return builder.toString();
    }

    private static <T> Field[] getFieldArrays(Class<?> entityClass) {
        List<Field> fields = new ArrayList();
        if (!entityClass.equals(Object.class)) {
            Class<?> superClass = entityClass.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                fields.addAll(Arrays.asList(getFieldArrays(superClass)));
            }

            fields.addAll(Arrays.asList(entityClass.getDeclaredFields()));
        }

        return (Field[]) fields.toArray(new Field[0]);
    }
}
