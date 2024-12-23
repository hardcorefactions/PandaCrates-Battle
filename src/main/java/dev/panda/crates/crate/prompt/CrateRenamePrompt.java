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

public class CrateRenamePrompt
extends StringPrompt {
    private final Crate crate;

    public String getPromptText(ConversationContext conversationContext) {
        return CC.translate("&7Enter a new display name for the crate: &6" + this.crate.getName() + "&7. Or type &6cancel&7 to cancel.");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.translate("&cCrate rename cancelled."));
            new CrateEditMenu(this.crate).openMenu(player);
            return Prompt.END_OF_CONVERSATION;
        }
        this.crate.setDisplayName(CC.translate(input));
        player.sendMessage(CC.translate("&aCrate displayname change to &6" + this.crate.getDisplayName() + "&a."));
        this.crate.respawnHolograms();
        new CrateEditMenu(this.crate).openMenu(player);
        return Prompt.END_OF_CONVERSATION;
    }

    public CrateRenamePrompt(Crate crate) {
        this.crate = crate;
    }
}

