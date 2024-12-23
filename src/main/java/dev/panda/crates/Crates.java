/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dev.panda.crates;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.hologram.Hologram;
import dev.panda.crates.loader.CratesLoader;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class Crates
extends JavaPlugin {
    @Getter
    private static Crates instance;
    @Getter
    private CratesLoader loader;
    @Getter
    @Setter
    private boolean serverLoaded = false;

    public void onEnable() {
        instance = this;
        this.loader = new CratesLoader(this);
    }

    public void onDisable() {
        Crate.getCrates().values().forEach(crate -> {
            crate.getHolograms().values().forEach(Hologram::remove);
            crate.save();
        });
    }

}

