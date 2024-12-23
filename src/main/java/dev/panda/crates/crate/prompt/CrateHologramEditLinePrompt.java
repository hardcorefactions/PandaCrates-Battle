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
import dev.panda.crates.crate.menus.hologram.EditHologramLinesMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateHologramEditLinePrompt
extends StringPrompt {
    private final Crate crate;
    private int index;

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return CC.YELLOW + "You're now editing the line " + (this.index + 1) + " of the hologram " + this.crate.getDisplayName() + CC.YELLOW + ". " + CC.GRAY + "Type " + CC.YELLOW + "next" + CC.GRAY + " to edit the next line " + CC.GRAY + "or type '" + CC.RED + "cancel" + CC.GRAY + "' to cancel the operation.";
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
        if (s.equalsIgnoreCase("next")) {
            if (this.crate.getHologramLines().size() > this.index + 1) {
                player.sendMessage(CC.RED + "You're already on the last line.");
                return this;
            }
            ++this.index;
            return this;
        }
        if (this.crate.getHologramLines().isEmpty()) {
            this.crate.getHologramLines().add(CC.translate(s));
        } else {
            this.crate.getHologramLines().set(this.index, CC.translate(s));
        }
        this.crate.respawnHolograms();
        player.sendMessage(CC.GREEN + "You've successfully modified the line of the hologram.");
        EditHologramLinesMenu menu = new EditHologramLinesMenu(this.crate);
        menu.selected = this.index + 1 > this.crate.getHologramsLines().size() - 1 ? 0 : this.index + 1;
        menu.open(player);
        return END_OF_CONVERSATION;
    }

    public CrateHologramEditLinePrompt(Crate crate, int index) {
        this.crate = crate;
        this.index = index;
    }
}

