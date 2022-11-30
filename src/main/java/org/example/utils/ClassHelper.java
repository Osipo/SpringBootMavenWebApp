package org.example.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ClassHelper {

    public static List<Field> getPublicStaticFields(Class<?> clazz){
        return Arrays.stream(clazz.getFields()).filter(x -> (x.getModifiers() & Modifier.STATIC) != 0).collect(Collectors.toList());
    }

    public static List<Object> getNonNullFieldValues(List<Field> fields, Object instance) {
        return fields.stream().map(x -> f_getValue(x, instance)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static Map<String, Object> getPublicStaticFieldsWithValues(Class<?> clazz){
        Map<String, Object> result = new TreeMap<>();
        for(Field f: getPublicStaticFields(clazz)){
            result.put(f.getName(), f_getValue(f, null));
        }
        return result;
    }

    private static Object f_getValue(Field f, Object instance){
        Object result = null;
        try{
            result = f.get(instance);
        }
        catch (IllegalAccessException | IllegalArgumentException e){}
        return result;
    }
}
