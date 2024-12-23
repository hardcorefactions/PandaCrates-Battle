/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package dev.panda.crates.managers.hologram;

import dev.panda.crates.managers.hologram.nms.HologramNMS;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class NMSManager {
    private HologramNMS hologramNMS;
    private final Pattern VERSION_PATTERN = Pattern.compile("(\\d_)(\\d*)(_R\\d)");

    public NMSManager() {
        String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        Matcher matcher = this.VERSION_PATTERN.matcher(SERVER_VERSION);
        int SERVER_VERSION_INT = matcher.find() ? Integer.parseInt(matcher.group(2)) : -1;
        HologramNMS hologramNMS = this.getHologramNMSByVersion(SERVER_VERSION_INT);
        if (hologramNMS != null) {
            this.hologramNMS = hologramNMS;
        } else {
            Bukkit.getLogger().severe("[PandaCrates] This spigot version doesn't support built-in hologram system.");
        }
    }

    private HologramNMS getHologramNMSByVersion(int version) {
        String pack = "dev.panda.crates.managers.hologram.nms.impl.Hologram1_" + version;
        try {
            Class<?> clazz = Class.forName(pack);
            return (HologramNMS)clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

}

