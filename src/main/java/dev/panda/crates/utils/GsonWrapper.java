/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.utils;

import com.google.gson.Gson;

public class GsonWrapper {
    private static final Gson gson = new Gson();

    public static String getString(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T deserializeJson(String json, Class<T> type) {
        try {
            if (json == null) {
                return null;
            }
            T obj = gson.fromJson(json, type);
            return type.cast(obj);
        } catch (Exception ex) {
            throw new RuntimeException("Error while converting json to " + type.getName(), ex);
        }
    }
}

