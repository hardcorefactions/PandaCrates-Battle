/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.hologram;

import com.google.common.collect.Lists;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.hologram.HologramTask;
import dev.panda.crates.loader.CratesLoader;
import dev.panda.crates.managers.hologram.nms.HologramNMS;
import dev.panda.crates.utils.CratesUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Hologram {
    private static final HologramTask HOLOGRAM_TASK = new HologramTask();
    private final Crates plugin;
    private final HologramNMS hologramNMS;
    @Getter
    private final Location location;
    @Getter
    private final Crate crate;
    private List<Integer> ids;
    @Getter
    private final List<UUID> viewers;

    public Hologram(Crates plugin, CratesLoader loader, Location location, Crate crate) {
        this.plugin = plugin;
        this.location = location;
        this.crate = crate;
        this.ids = Lists.newArrayList();
        this.viewers = Lists.newArrayList();
        this.hologramNMS = loader.getNMSManager().getHologramNMS();
    }

    public void respawn(Player player) {
        this.remove(player);
        this.spawn(player);
    }

    public void spawn(Player player) {
        if (this.hologramNMS == null) {
            return;
        }
        if (this.viewers.contains(player.getUniqueId())) {
            return;
        }
        this.viewers.add(player.getUniqueId());
        this.ids.addAll(this.hologramNMS.spawn(player, this.location, CratesUtil.getHologramLines(this.crate).stream().map(line -> line.replace("{display}", this.crate.getDisplayName()).replace("{name}", this.crate.getName())).collect(Collectors.toList()), this.crate.getHologramItem() == null || this.crate.getHologramItem().getType() == Material.AIR ? null : this.crate.getHologramItem(), false));
    }

    public void remove(Player player) {
        if (this.hologramNMS == null) {
            return;
        }
        this.hologramNMS.remove(player, this.ids);
        this.viewers.remove(player.getUniqueId());
    }

    public void remove() {
        Bukkit.getOnlinePlayers().forEach(player -> this.remove(player));
    }

    public boolean isViewing(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    public Hologram(Crates plugin, HologramNMS hologramNMS, Location location, Crate crate, List<UUID> viewers) {
        this.plugin = plugin;
        this.hologramNMS = hologramNMS;
        this.location = location;
        this.crate = crate;
        this.viewers = viewers;
    }

    static {
        HOLOGRAM_TASK.runTaskTimerAsynchronously(Crates.getInstance(), 20L, 2L);
    }
}

