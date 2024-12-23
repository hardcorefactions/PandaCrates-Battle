/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.utils;

import dev.panda.crates.utils.chat.ChatUtil;
import dev.panda.crates.utils.compatibility.BukkitPackage;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public final class PlayerUtil {
    public static int getPing(Player player) {
        try {
            Class<?> craftPlayer = BukkitPackage.CRAFTBUKKIT.getClass("entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(player);
            return (Integer)handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getCountry(String IP) {
        String country = "";
        try {
            URL url = new URL("http://ip-api.com/json/" + IP + "?fields=country");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            country = in.readLine().trim();
            if (country.length() == 0) {
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    country = ip.getHostAddress().trim();
                } catch (Exception exp) {
                    country = "Not Found";
                }
            }
        } catch (Exception ex) {
            ChatUtil.log("Error in check country!");
        }
        return country.replace("{", "").replace("}", "").replace("\"\"", "").replace(":", "");
    }

    public static boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() < 0;
    }

    public static void decrement(Player player, ItemStack itemStack, boolean sound, boolean cursor) {
        if (sound) {
            CompatibleSound.ANVIL_BREAK.play(player);
        }
        if (itemStack.getAmount() <= 1) {
            if (cursor) {
                player.setItemOnCursor(null);
            } else {
                player.setItemInHand(null);
            }
        } else {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        player.updateInventory();
    }

    private PlayerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

