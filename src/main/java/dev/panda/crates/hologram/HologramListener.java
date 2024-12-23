/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.hologram;

import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.hologram.Hologram;
import dev.panda.crates.utils.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class HologramListener
implements Listener {
    public HologramListener(Crates plugin, boolean isValid) {
        if (!isValid) {
            return;
        }
        if (plugin.isServerLoaded()) {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        for (Crate crate : Crate.getCrates().values()) {
            for (Hologram hologram : crate.getHolograms().values()) {
                hologram.respawn(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        for (Crate crate : Crate.getCrates().values()) {
            for (Hologram hologram : crate.getHolograms().values()) {
                hologram.remove(event.getPlayer());
            }
        }
    }

    @EventHandler
    private void onChangeWorld(PlayerChangedWorldEvent event) {
        for (Crate crate : Crate.getCrates().values()) {
            for (Hologram hologram : crate.getHolograms().values()) {
                hologram.remove(event.getPlayer());
                if (!event.getPlayer().getWorld().equals(hologram.getLocation().getWorld())) continue;
                hologram.respawn(event.getPlayer());
            }
        }
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent event) {
        TaskUtil.runLater(() -> {
            for (Crate crate : Crate.getCrates().values()) {
                for (Hologram hologram : crate.getHolograms().values()) {
                    hologram.respawn(event.getPlayer());
                }
            }
        }, 5L);
    }
}

