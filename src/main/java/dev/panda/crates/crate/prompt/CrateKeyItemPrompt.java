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
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class CrateKeyItemPrompt
extends StringPrompt {
    private final Crate crate;

    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&7Put item in your hand and type &6accept&7 to confirm. Or type &6cancel&7 to cancel.");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.translate("&cCrate rename cancelled."));
            new CrateEditMenu(this.crate).openMenu(player);
            return Prompt.END_OF_CONVERSATION;
        }
        if (player.getItemInHand() == null) {
            player.sendMessage(CC.translate("&cYou must have an item in your hand to select it."));
            return this;
        }
        if (input.equalsIgnoreCase("accept")) {
            this.crate.setKey(player.getItemInHand());
            player.sendMessage(CC.translate("&aCrate key item set to &6" + player.getItemInHand().getType().name() + "&a."));
            new CrateEditMenu(this.crate).openMenu(player);
            return Prompt.END_OF_CONVERSATION;
        }
        return this;
    }

    public CrateKeyItemPrompt(Crate crate) {
        this.crate = crate;
    }
}

