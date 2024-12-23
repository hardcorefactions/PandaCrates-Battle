/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package dev.panda.crates.hologram;

import dev.panda.crates.crate.Crate;
import net.jafama.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramTask
extends BukkitRunnable {
    private static final double HOLOGRAM_VIEW = 32.0;

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.handle(player);
        }
    }

    private void handle(Player player) {
        Crate.getCrates().values().forEach(crate -> this.hologramCheck(player, crate));
    }

    private void hologramCheck(Player player, Crate crate) {
        Location position = player.getLocation();
        crate.getHolograms().forEach((location, hologram) -> {
            if (position.getWorld() != location.getWorld()) {
                return;
            }
            double distanceSquared = position.distanceSquared(location);
            double distance = FastMath.sqrtQuick(distanceSquared);
            if (distance < 32.0 && !hologram.isViewing(player)) {
                hologram.spawn(player);
            }
            if (distance > 32.0 && hologram.isViewing(player)) {
                hologram.remove(player);
            }
        });
    }
}

