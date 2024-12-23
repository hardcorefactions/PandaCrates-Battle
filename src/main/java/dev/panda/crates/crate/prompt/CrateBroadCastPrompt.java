/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.prompt;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateBroadcastMenu;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class CrateBroadCastPrompt
extends StringPrompt {
    private final Crate crate;
    private final Reward reward;
    private final LinkedList<String> lines;

    public CrateBroadCastPrompt(Crate crate, Reward reward) {
        this.reward = reward;
        this.crate = crate;
        this.lines = reward.getBroadcast();
    }

    public String getPromptText(ConversationContext context) {
        return CC.translate("&7Enter a lines to broadcast when the crate is opened. &7&oOr type &ccancel&7&o to cancel or type &aconfirm&7 to end: ");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("confirm")) {
            this.reward.setBroadcast(this.lines);
            new CrateBroadcastMenu(this.crate, this.reward).openMenu(player);
            return Prompt.END_OF_CONVERSATION;
        }
        this.lines.add(CC.translate(input.replace("{name}", this.crate.getDisplayName())));
        player.sendMessage(CC.translate("&aAdded new broadcast line"));
        return this;
    }
}

