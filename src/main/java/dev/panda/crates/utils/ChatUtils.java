/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.conversations.Conversable
 *  org.bukkit.conversations.ConversationFactory
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.utils;

import dev.panda.crates.Crates;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public final class ChatUtils {
    public static void beginPrompt(Player player, StringPrompt prompt) {
        player.closeInventory();
        player.beginConversation(new ConversationFactory(Crates.getInstance()).withFirstPrompt(prompt).withTimeout(60).withModality(false).withLocalEcho(false).buildConversation(player));
    }

    public static String getFirstColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.substring(0, 2));
    }

    private ChatUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

