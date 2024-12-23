/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.compatibility.particle;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface ParticleType {
    static ParticleType of(String name) {
        return ParticleTypes.of(name);
    }

    String getName();

    Class<?> getRawDataType();

    default Class<?> getDataType() {
        Class<?> type = this.getRawDataType();
        return ParticleTypes.DATA_ADAPTERS.getOrDefault(type, type);
    }

    default void spawn(World world, Location location, int count) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count);
    }

    default void spawn(World world, double x, double y, double z, int count) {
        this.spawn(world, x, y, z, count, null);
    }

    default <T> void spawn(World world, Location location, int count, T data) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count, data);
    }

    default <T> void spawn(World world, double x, double y, double z, int count, T data) {
        this.spawn(world, x, y, z, count, 0.0, 0.0, 0.0, data);
    }

    default void spawn(World world, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    default void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawn(world, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    default <T> void spawn(World world, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    default <T> void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawn(world, x, y, z, count, offsetX, offsetY, offsetZ, 1.0, data);
    }

    default void spawn(World world, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    default void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawn(world, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    default <T> void spawn(World world, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawn(world, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    <T> void spawn(World var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17);

    default void spawn(Player player, Location location, int count) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count);
    }

    default void spawn(Player player, double x, double y, double z, int count) {
        this.spawn(player, x, y, z, count, null);
    }

    default <T> void spawn(Player player, Location location, int count, T data) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count, data);
    }

    default <T> void spawn(Player player, double x, double y, double z, int count, T data) {
        this.spawn(player, x, y, z, count, 0.0, 0.0, 0.0, data);
    }

    default void spawn(Player player, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    default void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawn(player, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    default <T> void spawn(Player player, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    default <T> void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawn(player, x, y, z, count, offsetX, offsetY, offsetZ, 1.0, data);
    }

    default void spawn(Player player, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    default void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawn(player, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    default <T> void spawn(Player player, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawn(player, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    <T> void spawn(Player var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17);
}

