/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.managers.hologram.nms;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface HologramNMS {
    int getVersion();

    List<Integer> spawn(Player var1, Location var2, List<String> var3, ItemStack var4, boolean var5);

    void remove(Player var1, List<Integer> var2);
}

