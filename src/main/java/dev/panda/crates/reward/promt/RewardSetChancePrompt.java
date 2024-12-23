/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.reward.promt;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.menus.RewardEditMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class RewardSetChancePrompt
extends StringPrompt {
    private final Reward reward;
    private final Crate crate;

    public String getPromptText(ConversationContext context) {
        return CC.translate("&7Set the chance of this reward to be given. Or type &6cancel &7to go back.");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            new RewardEditMenu(this.reward, this.crate).openMenu(player);
            return END_OF_CONVERSATION;
        }
        try {
            double chance = Double.parseDouble(input);
            if (chance < 0.0 || chance > 1.0) {
                player.sendMessage(CC.translate("&cThe chance must be between 0 and 1.0."));
                return this;
            }
            this.reward.setChance(chance);
            player.sendMessage(CC.translate("&aThe chance of this reward is now &6" + chance + "&a%."));
            new RewardEditMenu(this.reward, this.crate).openMenu(player);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe chance must be a number between 0 and 1.0."));
            return this;
        }
        return END_OF_CONVERSATION;
    }

    public RewardSetChancePrompt(Reward reward, Crate crate) {
        this.reward = reward;
        this.crate = crate;
    }
}

