/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonChain {
    private final JsonObject json = new JsonObject();

    public JsonChain addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain add(String property, JsonElement element) {
        this.json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return this.json;
    }
}

