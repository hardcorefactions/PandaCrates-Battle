/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package dev.panda.crates.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    public static String parseLocation(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location convertLocation(String string) {
        String[] build = string.split(":");
        World world = Bukkit.getWorld(build[0]);
        double x = Double.parseDouble(build[1]);
        double y = Double.parseDouble(build[2]);
        double z = Double.parseDouble(build[3]);
        float yaw = Float.parseFloat(build[4]);
        float pitch = Float.parseFloat(build[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static Location getHighestLocation(Location origin) {
        return LocationUtil.getHighestLocation(origin, null);
    }

    public static List<Player> getNearbyPlayers(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
        ArrayList<Player> radiusEntities = new ArrayList<>();
        for (int chX = -chunkRadius; chX <= chunkRadius; ++chX) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; ++chZ) {
                int x = (int)l.getX();
                int y = (int)l.getY();
                int z = (int)l.getZ();
                for (Entity e : new Location(l.getWorld(), x + chX * 16, y, z + chZ * 16).getChunk().getEntities()) {
                    if (!(e instanceof Player) || !(e.getLocation().distance(l) <= (double)radius) || e.getLocation().getBlock() == l.getBlock()) continue;
                    radiusEntities.add((Player)e);
                }
            }
        }
        return radiusEntities;
    }

    public static Location getHighestLocation(Location origin, Location def) {
        Preconditions.checkNotNull(origin, "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block;
            if ((block = world.getBlockAt(x, --y, z)).isEmpty()) continue;
            Location next = block.getLocation();
            next.setPitch(origin.getPitch());
            next.setYaw(origin.getYaw());
            return next;
        }
        return def;
    }

    public static void multiplyVelocity(Player player, Vector vector, double multiply, double addY) {
        vector.normalize();
        vector.multiply(multiply);
        vector.setY(vector.getY() + addY);
        player.setFallDistance(0.0f);
        player.setVelocity(vector);
    }
}

