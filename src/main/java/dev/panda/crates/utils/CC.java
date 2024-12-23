/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package dev.panda.crates.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public final class CC {
    public static final String YELLOW = ChatColor.YELLOW.toString();
    public static String GOLD = ChatColor.GOLD.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String PINK;
    public static final String LIGHT_PURPLE;
    public static final String GREEN;
    public static final String WHITE;
    public static final String BLACK;
    public static final String AQUA;
    public static final String GRAY;
    public static final String DARK_BLUE;
    public static final String D_BLUE;
    public static final String DARK_GREEN;
    public static final String D_GREEN;
    public static final String DARK_AQUA;
    public static final String D_AQUA;
    public static final String DARK_RED;
    public static final String D_RED;
    public static final String DARK_PURPLE;
    public static final String D_PURPLE;
    public static final String PURPLE;
    public static final String DARK_GRAY;
    public static final String D_GRAY;
    public static final String BOLD;
    public static final String ITALIC;
    public static final String STRIKETHROUGH;
    public static final String MAGIC;
    public static final String UNDERLINE;
    public static final String RESET;
    public static final String CHAT_BAR;
    public static final String STRAIGHT_BAR;
    public static final String C_STRAIGHT_BAR;
    public static final String CR_STRAIGHT_BAR;
    public static final String LEFT_ARROW;
    public static final String C_LEFT_ARROW;
    public static final String A_LEFT_ARROW;
    public static final String SQUARE;
    public static final String C_SQUARE;

    public static String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> format(List<String> input) {
        return input.stream().map(CC::format).collect(Collectors.toList());
    }

    public static String translate(String input) {
        return CC.format(input);
    }

    public static List<String> translate(List<String> input) {
        return CC.format(input);
    }

    private CC() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        LIGHT_PURPLE = PINK = ChatColor.LIGHT_PURPLE.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        AQUA = ChatColor.AQUA.toString();
        GRAY = ChatColor.GRAY.toString();
        D_BLUE = DARK_BLUE = ChatColor.DARK_BLUE.toString();
        D_GREEN = DARK_GREEN = ChatColor.DARK_GREEN.toString();
        D_AQUA = DARK_AQUA = ChatColor.DARK_AQUA.toString();
        D_RED = DARK_RED = ChatColor.DARK_RED.toString();
        D_PURPLE = DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        PURPLE = DARK_PURPLE;
        D_GRAY = DARK_GRAY = ChatColor.DARK_GRAY.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        STRIKETHROUGH = ChatColor.STRIKETHROUGH.toString();
        MAGIC = ChatColor.MAGIC.toString();
        UNDERLINE = ChatColor.UNDERLINE.toString();
        RESET = ChatColor.RESET.toString();
        CHAT_BAR = D_GRAY + STRIKETHROUGH + StringUtils.repeat('-', 40);
        STRAIGHT_BAR = "┃";
        C_STRAIGHT_BAR = BLUE + BOLD + "┃";
        CR_STRAIGHT_BAR = BLUE + BOLD + "┃ " + GRAY;
        LEFT_ARROW = "➥";
        C_LEFT_ARROW = BLUE + LEFT_ARROW + " " + AQUA;
        A_LEFT_ARROW = AQUA + LEFT_ARROW + " ";
        SQUARE = "▪";
        C_SQUARE = AQUA + BOLD + SQUARE + " " + GRAY;
    }
}

