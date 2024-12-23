/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.utils.fancy;

import com.google.gson.stream.JsonWriter;

import java.io.IOException;

interface JsonRepresentedObject {
    void writeJson(JsonWriter var1) throws IOException;
}

