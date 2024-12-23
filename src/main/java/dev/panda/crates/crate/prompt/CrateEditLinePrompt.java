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

public class CrateEditLinePrompt
extends StringPrompt {
    private final Crate crate;
    private final Reward reward;
    private final int index;

    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&7Enter a text to modify the line number &6" + this.index + "&7. Or type &ccancel&7 to cancel.");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            new CrateBroadcastMenu(this.crate, this.reward).openMenu(player);
            return Prompt.END_OF_CONVERSATION;
        }
        this.reward.getBroadcast().set(this.index, CC.translate(input.replace("{name}", this.crate.getDisplayName())));
        new CrateBroadcastMenu(this.crate, this.reward).openMenu(player);
        return Prompt.END_OF_CONVERSATION;
    }

    public CrateEditLinePrompt(Crate crate, Reward reward, int index) {
        this.crate = crate;
        this.reward = reward;
        this.index = index;
    }
}

