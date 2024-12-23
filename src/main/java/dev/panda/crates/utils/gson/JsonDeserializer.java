/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.utils.gson;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {
    T deserialize(JsonObject var1);
}

