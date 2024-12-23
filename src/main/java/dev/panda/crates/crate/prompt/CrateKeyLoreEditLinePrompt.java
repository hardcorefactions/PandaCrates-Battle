/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.panda.crates.crate.prompt;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.key.EditKeyLoreLinesMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateKeyLoreEditLinePrompt
extends StringPrompt {
    private final Crate crate;
    private final int index;

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return CC.YELLOW + "You're now editing the line " + (this.index + 1) + " of the key lore " + this.crate.getDisplayName() + CC.YELLOW + ". " + CC.GRAY + "Type '" + CC.RED + "cancel" + CC.GRAY + "' to cancel the operation.";
    }

    @Nullable
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String s) {
        if (s == null) {
            return this;
        }
        Player player = (Player)context.getForWhom();
        if (s.equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.RED + "You've canceled the operation.");
            return END_OF_CONVERSATION;
        }
        if (this.crate.getKeyLoreLines().isEmpty()) {
            this.crate.getKeyLoreLines().add(CC.translate(s));
        } else {
            this.crate.getKeyLoreLines().set(this.index, CC.translate(s));
        }
        player.sendMessage(CC.GREEN + "You've successfully modified the line of the key lore.");
        EditKeyLoreLinesMenu menu = new EditKeyLoreLinesMenu(this.crate);
        menu.selected = this.index + 1 > this.crate.getKeyLoreLines().size() - 1 ? 0 : this.index + 1;
        menu.open(player);
        return END_OF_CONVERSATION;
    }

    public CrateKeyLoreEditLinePrompt(Crate crate, int index) {
        this.crate = crate;
        this.index = index;
    }
}

