/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package dev.panda.crates.utils.fancy;

import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class JsonString
implements JsonRepresentedObject,
ConfigurationSerializable {
    private final String _value;

    public JsonString(CharSequence value) {
        this._value = value == null ? null : value.toString();
    }

    public static JsonString deserialize(Map<String, Object> map) {
        return new JsonString(map.get("stringValue").toString());
    }

    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.value(this.getValue());
    }

    public String getValue() {
        return this._value;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> theSingleValue = new HashMap<>();
        theSingleValue.put("stringValue", this._value);
        return theSingleValue;
    }

    public String toString() {
        return this._value;
    }
}

