/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityArmorStand
 *  net.minecraft.server.v1_8_R3.EntityItem
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.PlayerConnection
 *  net.minecraft.server.v1_8_R3.World
 *  net.minecraft.server.v1_8_R3.WorldServer
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.managers.hologram.nms.impl;

import com.google.common.collect.Lists;
import dev.panda.crates.managers.hologram.HologramUtil;
import dev.panda.crates.managers.hologram.nms.HologramNMS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hologram1_8
implements HologramNMS {
    @Override
    public int getVersion() {
        return 8;
    }

    @Override
    public List<Integer> spawn(Player player, Location location, List<String> lines, ItemStack hologramItem, boolean isSkull) {
        ArrayList<Integer> ids = Lists.newArrayList();
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        WorldServer world = ((CraftWorld)location.getWorld()).getHandle();
        Location loc = HologramUtil.getHologramLocation(location, lines, isSkull);
        if (hologramItem != null) {
            loc.add(0.0, 0.25, 0.0);
            EntityArmorStand stand = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
            EntityItem item = new EntityItem(world, loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(hologramItem));
            stand.n(true);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setBasePlate(false);
            item.motX = 0.0;
            item.motY = 0.0;
            item.motZ = 0.0;
            item.v();
            item.mount(stand);
            ids.addAll(Arrays.asList(stand.getId(), item.getId()));
            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
            connection.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            connection.sendPacket(new PacketPlayOutSpawnEntity(item, 2));
            connection.sendPacket(new PacketPlayOutEntityMetadata(item.getId(), item.getDataWatcher(), true));
            connection.sendPacket(new PacketPlayOutEntityVelocity(item));
            connection.sendPacket(new PacketPlayOutAttachEntity(0, item, stand));
            loc.subtract(0.0, 0.49, 0.0);
        }
        for (String message : lines) {
            if (message.isEmpty() || message.equals("{spacer}") || HologramUtil.COLOR_PATTERN.matcher(message).matches()) {
                loc.subtract(0.0, 0.24, 0.0);
                continue;
            }
            EntityArmorStand stand = new EntityArmorStand(world, loc.getX(), loc.getY(), loc.getZ());
            stand.n(true);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', message));
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setBasePlate(false);
            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));
            connection.sendPacket(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            ids.add(stand.getId());
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

