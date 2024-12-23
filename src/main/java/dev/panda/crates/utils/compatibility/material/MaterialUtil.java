/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package dev.panda.crates.utils.compatibility.material;

import org.bukkit.Material;

public final class MaterialUtil {
    public static Material getMaterial(String material) {
        return Material.getMaterial(material);
    }

    private MaterialUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

