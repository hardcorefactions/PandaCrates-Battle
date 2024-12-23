/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.DataWatcher
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.EntityHorse
 *  net.minecraft.server.v1_7_R4.EntityItem
 *  net.minecraft.server.v1_7_R4.EntityLiving
 *  net.minecraft.server.v1_7_R4.EntityWitherSkull
 *  net.minecraft.server.v1_7_R4.EnumEntitySize
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity
 *  net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity
 *  net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  net.minecraft.server.v1_7_R4.World
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.managers.hologram.nms.impl;

import com.google.common.collect.Lists;
import dev.panda.crates.managers.hologram.HologramUtil;
import dev.panda.crates.managers.hologram.nms.HologramNMS;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityItem;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.EnumEntitySize;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hologram1_7
implements HologramNMS {
    @Override
    public int getVersion() {
        return 7;
    }

    @Override
    public List<Integer> spawn(Player player, Location location, List<String> lines, ItemStack hologramItem, boolean isSkull) {
        ArrayList<Integer> ids = Lists.newArrayList();
        WorldServer world = ((CraftWorld)location.getWorld()).getHandle();
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        boolean using18 = connection.networkManager.getVersion() > 5;
        Location loc = HologramUtil.getHologramLocation(location, lines, isSkull, using18);
        if (hologramItem != null) {
            EntityWitherSkull skull = new EntityWitherSkull(world);
            EntityItem item = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(hologramItem));
            skull.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
            skull.setInvisible(true);
            item.motX = 0.0;
            item.motY = 0.0;
            item.motZ = 0.0;
            item.age = 5999;
            item.pickupDelay = Short.MAX_VALUE;
            item.mount(skull);
            ids.addAll(Arrays.asList(skull.getId(), item.getId()));
            connection.sendPacket(new PacketPlayOutSpawnEntity(skull, using18 ? 78 : 66));
            connection.sendPacket(new PacketPlayOutEntityMetadata(skull.getId(), skull.getDataWatcher(), true));
            connection.sendPacket(new PacketPlayOutSpawnEntity(item, 2));
            connection.sendPacket(new PacketPlayOutEntityMetadata(item.getId(), item.getDataWatcher(), true));
            connection.sendPacket(new PacketPlayOutEntityVelocity(item));
            connection.sendPacket(new PacketPlayOutAttachEntity(0, item, skull));
        }
        loc.subtract(0.0, !using18 ? (double)lines.size() * 0.24 + 0.5 : 0.0, 0.0).add(0.0, using18 ? 0.85 : 55.75, 0.0);
        for (String message : lines) {
            if (message.isEmpty() || message.equals("{spacer}") || HologramUtil.COLOR_PATTERN.matcher(message).matches()) {
                loc.subtract(0.0, 0.24, 0.0);
                continue;
            }
            EntityWitherSkull skull = new EntityWitherSkull(world);
            skull.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
            skull.setInvisible(true);
            EntityHorse horse = new EntityHorse(world);
            horse.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
            String line = ChatColor.translateAlternateColorCodes('&', message);
            if (using18) {
                DataWatcher dataWatcher = horse.getDataWatcher();
                try {
                    Field field = dataWatcher.getClass().getDeclaredField("dataValues");
                    field.setAccessible(true);
                    Map dataValues = (Map)field.get(dataWatcher);
                    dataValues.clear();
                    field.set(horse.getDataWatcher(), dataValues);
                    field.setAccessible(false);
                } catch (Exception ex) {
                    throw new IllegalStateException("Cannot access 'dataValues' field. Please contact with Owner's PandaCommunity");
                }
                horse.getDataWatcher().a(0, 32);
                horse.getDataWatcher().a(2, line.length() > 256 ? line.substring(0, 256) : line);
                horse.getDataWatcher().a(3, 1);
                horse.getDataWatcher().a(10, 16);
            } else {
                horse.setCustomName(line);
                horse.setCustomNameVisible(true);
                horse.boundingBox.a = 0.0;
                horse.boundingBox.b = 0.0;
                horse.boundingBox.c = 0.0;
                horse.boundingBox.d = 0.0;
                horse.boundingBox.e = 0.0;
                horse.boundingBox.f = 0.0;
                horse.setAge(-1700000);
                horse.ageLocked = true;
                horse.persistent = true;
                horse.width = 0.0f;
                horse.height = 0.0f;
                horse.as = EnumEntitySize.SIZE_1;
                horse.mount(skull);
            }
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(horse);
            if (using18) {
                try {
                    Field fieldType = packet.getClass().getDeclaredField("b");
                    fieldType.setAccessible(true);
                    fieldType.set(packet, 30);
                } catch (Exception ignored) {
                    throw new IllegalStateException("Cannot change packet values. Please contact with Panda Community.");
                }
            }
            connection.sendPacket(packet);
            connection.sendPacket(new PacketPlayOutEntityMetadata(horse.getId(), horse.getDataWatcher(), true));
            if (using18) {
                ids.add(horse.getId());
            } else {
                connection.sendPacket(new PacketPlayOutSpawnEntity(skull, 66));
                connection.sendPacket(new PacketPlayOutEntityMetadata(skull.getId(), skull.getDataWatcher(), true));
                connection.sendPacket(new PacketPlayOutAttachEntity(0, horse, skull));
                ids.addAll(Arrays.asList(skull.getId(), horse.getId()));
            }
            loc.subtract(0.0, 0.24, 0.0);
        }
        return ids;
    }

    @Override
    public void remove(Player player, List<Integer> ids) {
        for (Integer id : ids) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]{id}));
        }
    }
}

