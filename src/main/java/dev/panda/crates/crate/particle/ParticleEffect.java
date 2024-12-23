/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.crate.particle;

import com.google.common.collect.Maps;
import dev.panda.crates.Crates;
import dev.panda.crates.utils.compatibility.particle.ParticleType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
public class ParticleEffect {
    private final ParticleType particle;
    private final ParticleShape shape;
    private final Map<Location, Integer> tasks = Maps.newHashMap();

    public void play(Location location) {
        Runnable runnable = this.shape.play(this.particle, location);
        if (runnable == null) {
            return;
        }
        this.tasks.put(location, Bukkit.getScheduler().runTaskTimerAsynchronously(Crates.getInstance(), runnable, 5L, TimeUnit.MILLISECONDS.toMillis(1L)).getTaskId());
    }

    public void safePlay(@Nullable Location location) {
        if (location == null) {
            return;
        }
        this.play(location);
    }

    public void stop(Location location) {
        Integer id = this.tasks.get(location);
        if (id == null) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
    }

    public void stop() {
        for (int id : this.tasks.values()) {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public ParticleEffect(ParticleType particle, ParticleShape shape) {
        this.particle = particle;
        this.shape = shape;
    }
}

