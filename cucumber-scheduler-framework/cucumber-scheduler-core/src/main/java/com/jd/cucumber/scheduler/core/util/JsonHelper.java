package com.jd.cucumber.scheduler.core.util;

import com.google.gson.Gson;

public abstract class JsonHelper {

    private static final Gson GSON = new Gson();

    public static byte[] toBytes(Object object) {
        return StringHelper.getBytes(toJson(object));
    }

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(byte[] bytes, Class<T> clazz) {
        return fromJson(StringHelper.getString(bytes), clazz);
    }

}
