/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 */
package dev.panda.crates.utils;

import com.google.common.collect.Lists;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.hologram.Hologram;
import dev.panda.crates.utils.chat.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CratesUtil {
    public static String serializeLocation(Map<Location, Hologram> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Location, Hologram> entry : map.entrySet()) {
            Location location = entry.getKey();
            Hologram hologram = entry.getValue();
            builder.append(location.getWorld().getName()).append(";").append(location.getX()).append(";").append(location.getY()).append(";").append(location.getZ()).append(";").append(hologram.getLocation().getWorld().getName()).append(";").append(hologram.getLocation().getX()).append(";").append(hologram.getLocation().getY()).append(";").append(hologram.getLocation().getZ()).append(":");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static Map<Location, Hologram> deserializeLocation(Crates plugin, String serialized, Crate crate) {
        String[] args = serialized.split(":");
        HashMap<Location, Hologram> map = new HashMap<>();
        for (String arg : args) {
            String[] loc = arg.split(";");
            Location location = new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
            Hologram hologram = new Hologram(plugin, Crate.loader, new Location(Bukkit.getWorld(loc[4]), Double.parseDouble(loc[5]), Double.parseDouble(loc[6]), Double.parseDouble(loc[7])), crate);
            map.put(location, hologram);
        }
        return map;
    }

    public static List<String> getHologramLines(Crate crate) {
        ArrayList<String> lines = Lists.newArrayList();
        for (String line : crate.getHologramsLines()) {
            lines.add(ChatUtil.translate(line.replace("{crate_name}", crate.getName()).replace("{crate_displayname}", crate.getDisplayName())));
        }
        return lines;
    }

    private CratesUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

