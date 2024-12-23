/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package dev.panda.crates.utils.compatibility;

import org.bukkit.Bukkit;

public enum BukkitPackage {
    MINECRAFT("net.minecraft.server." + BukkitPackage.getServerVersion()),
    CRAFTBUKKIT("org.bukkit.craftbukkit." + BukkitPackage.getServerVersion());

    private final String path;

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public String toString() {
        return this.path;
    }

    public Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName(this.toString() + "." + className);
    }

    BukkitPackage(String path) {
        this.path = path;
    }
}

