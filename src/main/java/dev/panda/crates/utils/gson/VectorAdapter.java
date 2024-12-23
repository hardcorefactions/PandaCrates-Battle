/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.util.Vector
 */
package dev.panda.crates.utils.gson;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.*;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;

public class VectorAdapter
implements JsonDeserializer<Vector>,
JsonSerializer<Vector> {
    @Override
    public Vector deserialize(JsonElement src, Type type, JsonDeserializationContext context) throws JsonParseException {
        return VectorAdapter.fromJson(src);
    }

    @Override
    public JsonElement serialize(Vector src, Type type, JsonSerializationContext context) {
        return VectorAdapter.toJson(src);
    }

    public static JsonObject toJson(Vector src) {
        if (src == null) {
            return null;
        }
        JsonObject object = new JsonObject();
        object.addProperty("x", src.getX());
        object.addProperty("y", src.getY());
        object.addProperty("z", src.getZ());
        return object;
    }

    public static Vector fromJson(JsonElement src) {
        if (src == null || !src.isJsonObject()) {
            return null;
        }
        JsonObject json = src.getAsJsonObject();
        double x = json.get("x").getAsDouble();
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble();
        return new Vector(x, y, z);
    }
}

