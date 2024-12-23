/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 */
package dev.panda.crates.managers.hologram;

import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class HologramUtil {
    public static final Pattern COLOR_PATTERN = Pattern.compile("[§&][0-9a-f]");

    public static Location getHologramLocation(Location blockLocation, List<String> lines, boolean isSkull, boolean playerUsing8) {
        return blockLocation.clone().add(0.0, Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1).replace("1_", "").replaceAll("_R\\d", "")) > 7 ? (double)lines.size() * (isSkull ? 0.14 : 0.24) : (double)lines.size() * (isSkull ? 0.14 : 0.24) - (playerUsing8 ? 1.15 : 0.0) + (!playerUsing8 ? 0.15 : 0.0), 0.0);
    }

    public static Location getHologramLocation(Location blockLocation, List<String> lines, boolean isSkull) {
        return blockLocation.clone().add(0.0, (double)lines.size() * (isSkull ? 0.14 : 0.24), 0.0);
    }

    private HologramUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

