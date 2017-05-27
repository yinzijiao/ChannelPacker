package com.packer.utils;

import com.google.gson.JsonElement;

/**
 * Created by yin on 2017/5/27.
 */
public class ClassMake {
    static final String FIELD = "private %s %s;\n\n";
    static final String SET_METHOD = "public void set%s(%s %s){\n%sthis.%s = %s;\n%s}\n\n";
    static final String GET_METHOD = "public %s get%s(){\n%sreturn %s;\n%s}\n\n";


    static final String FIELD_LIST = "private List<%s> %s;\n\n";
    static final String SET_METHOD_LIST = "public void set%s(List<%s> %s){\n%sthis.%s = %s;\n%s}\n\n";
    static final String GET_METHOD_LIST = "public List<%s> get%s(){\n%sreturn %s;\n%s}\n\n";

    static final String PUBLIC_CLASS = "public class %s{\n\n";
    static final String CLASS = "class %s{\n\n";
    public static final String CLASS_END = "}\n\n";
    public static final String LIST_IMPORT = "import java.util.List;\n\n";

    public static String makeField(String name, String clazz, int count) {
        return tab(count) + String.format(FIELD, clazz, name);
    }

    public static String makeFieldList(String name, String clazz, int count) {
        return tab(count) + String.format(FIELD_LIST, clazz, name);
    }

    public static String makeField(String name, int count) {
        return tab(count) + String.format(FIELD, name.substring(0, 1).toUpperCase() + name.substring(1), name);
    }

    public static String makeClass(String name, int count) {
        return tab(count) + String.format(CLASS, name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    public static String makePublicClass(String name) {
        return String.format(PUBLIC_CLASS, name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    public static String makeGet(String name, String clazz, int count) {
        return tab(count) + String.format(GET_METHOD, clazz, name.substring(0, 1).toUpperCase() + name.substring(1), tab(count + 1), name, tab(count));
    }

    public static String makeSet(String name, String clazz, int count) {
        return tab(count) + String.format(SET_METHOD, name.substring(0, 1).toUpperCase() + name.substring(1), clazz, name, tab(count + 1), name, name, tab(count));
    }

    public static String makeGetList(String name, String clazz, int count) {
        return tab(count) + String.format(GET_METHOD_LIST, clazz, name.substring(0, 1).toUpperCase() + name.substring(1), tab(count + 1), name, tab(count));
    }

    public static String makeSetList(String name, String clazz, int count) {
        return tab(count) + String.format(SET_METHOD_LIST, name.substring(0, 1).toUpperCase() + name.substring(1), clazz, name, tab(count + 1), name, name, tab(count));
    }


    public static String getClazz(JsonElement element, String c) {
        String r = c.substring(0, 1).toUpperCase() + c.substring(1);
        if (element.isJsonPrimitive()) {
            if (element.getAsJsonPrimitive().isBoolean()) r = "boolean";
            if (element.getAsJsonPrimitive().isString()) r = "String";
            if (element.getAsJsonPrimitive().isNumber()) {
                double value = element.getAsNumber().doubleValue();
                if (value == (int) value) {
                    r = "int";
                } else {
                    r = "double";
                }
            }
        }
        return r;
    }

    public static String tab(int count) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            buffer.append("\t");
        }
        return buffer.toString();
    }

}
