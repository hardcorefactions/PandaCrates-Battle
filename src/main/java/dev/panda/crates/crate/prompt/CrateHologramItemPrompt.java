/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.panda.crates.crate.prompt;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateHologramItemPrompt
extends StringPrompt {
    private final Crate crate;

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return CC.YELLOW + "Please hold the item you want in your hand and then type '" + CC.GREEN + "confirm" + CC.YELLOW + "' to confirm or " + CC.RED + "cancel " + CC.YELLOW + "to cancel.";
    }

    @Nullable
    public Prompt acceptInput(@NotNull ConversationContext conversationContext, @Nullable String s) {
        if (s == null) {
            return this;
        }
        Player player = (Player)conversationContext.getForWhom();
        if (s.equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.RED + "You canceled the operation.");
            return END_OF_CONVERSATION;
        }
        if (s.equalsIgnoreCase("confirm")) {
            ItemStack stack = player.getItemInHand();
            if (stack.getType() == Material.AIR) {
                this.crate.setHologramItem(null);
            } else {
                ItemStack hologramItem = stack.clone();
                hologramItem.setAmount(1);
                this.crate.setHologramItem(hologramItem);
            }
            this.crate.respawnHolograms();
            player.sendMessage(CC.YELLOW + "You've modified the hologram item for crate " + this.crate.getDisplayName() + CC.YELLOW + ".");
            return END_OF_CONVERSATION;
        }
        return this;
    }

    public CrateHologramItemPrompt(Crate crate) {
        this.crate = crate;
    }
}

