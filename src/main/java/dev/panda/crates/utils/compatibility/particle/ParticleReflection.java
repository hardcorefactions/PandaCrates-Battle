/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package dev.panda.crates.utils.compatibility.particle;

import org.bukkit.Bukkit;

import java.util.Optional;

public final class ParticleReflection {
    public static final String OBC_PACKAGE = "org.bukkit.craftbukkit";
    public static final String NMS_PACKAGE = "net.minecraft.server";
    public static final String VERSION = Bukkit.getServer().getClass().getSimpleName().equals("CraftServer") ? Bukkit.getServer().getClass().getPackage().getName().substring("org.bukkit.craftbukkit".length() + 1) : "unknown";

    private ParticleReflection() {
        throw new UnsupportedOperationException();
    }

    public static String nmsClassName(String className) {
        return "net.minecraft.server." + VERSION + '.' + className;
    }

    public static Class<?> nmsClass(String className) throws ClassNotFoundException {
        return Class.forName(ParticleReflection.nmsClassName(className));
    }

    public static Optional<Class<?>> nmsOptionalClass(String className) {
        return ParticleReflection.optionalClass(ParticleReflection.nmsClassName(className));
    }

    public static String obcClassName(String className) {
        return "org.bukkit.craftbukkit." + VERSION + '.' + className;
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(ParticleReflection.obcClassName(className));
    }

    public static Optional<Class<?>> obcOptionalClass(String className) {
        return ParticleReflection.optionalClass(ParticleReflection.obcClassName(className));
    }

    public static Optional<Class<?>> optionalClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static Object enumValueOf(Class<?> enumClass, String enumName) {
        return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
    }
}

