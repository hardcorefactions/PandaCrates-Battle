/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.chat;

import com.google.common.base.Joiner;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ChatUtil {
    public static String LONG_LINE = "&7&m----------------------------------------";
    public static final String NORMAL_LINE = "&7&m-----------------------------";
    public static String SHORT_LINE = "&7&m---------------";
    public static String BLUE = ChatColor.BLUE.toString();
    public static String AQUA = ChatColor.AQUA.toString();
    public static String YELLOW = ChatColor.YELLOW.toString();
    public static String RED = ChatColor.RED.toString();
    public static String GRAY = ChatColor.GRAY.toString();
    public static String GOLD = ChatColor.GOLD.toString();
    public static String GREEN = ChatColor.GREEN.toString();
    public static String WHITE = ChatColor.WHITE.toString();
    public static String BLACK = ChatColor.BLACK.toString();
    public static String BOLD = ChatColor.BOLD.toString();
    public static String ITALIC = ChatColor.ITALIC.toString();
    public static String UNDER_LINE = ChatColor.UNDERLINE.toString();
    public static String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public static String RESET = ChatColor.RESET.toString();
    public static String MAGIC = ChatColor.MAGIC.toString();
    public static String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static String DARK_RED = ChatColor.DARK_RED.toString();
    public static String PINK = ChatColor.LIGHT_PURPLE.toString();
    private static final Joiner NEW_LINE_JOINER = Joiner.on("\n");

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String joinNewLine(List<String> list) {
        return NEW_LINE_JOINER.join(list);
    }

    public static List<String> translate(List<String> lines) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public static String strip(String text) {
        return ChatColor.stripColor(text);
    }

    public static String placeholder(Player player, String text, boolean isPlaceholderAPI, boolean colorized) {
        if (colorized) {
            return ChatUtil.translate(isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, text) : text);
        }
        return isPlaceholderAPI ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }

    public static void sendMessage(CommandSender sender, String text) {
        try {
            sender.sendMessage(ChatUtil.translate(text));
        } catch (Exception e) {
            throw new IllegalArgumentException("Message cannot be found, please check Lang configs or contact with the Authors");
        }
    }

    public static void broadcast(String text) {
        Bukkit.broadcastMessage(ChatUtil.translate(text));
    }

    public static void log(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatUtil.translate(text));
    }

    public static String toReadable(Enum<?> enu) {
        return ChatUtil.capitalize(enu.name());
    }

    public static String capitalize(String input) {
        if (input.isEmpty()) {
            return input;
        }
        String first = input.charAt(0) + "";
        return first.toUpperCase() + (input.length() > 1 ? input.substring(1) : "");
    }

    private ChatUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

