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

import com.google.common.collect.Lists;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.RewardType;
import dev.panda.crates.reward.menus.RewardEditMenu;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class RewardAddCommandPrompt
extends StringPrompt {
    private final Reward reward;
    private final Crate crate;
    private final Menu menu;

    public String getPromptText(ConversationContext context) {
        return CC.translate("&7Enter the command you want to add. Or type &6cancel&7 to go back.");
    }

    public Prompt acceptInput(ConversationContext context, String input) {
        Player player = (Player)context.getForWhom();
        if (input.equalsIgnoreCase("cancel")) {
            new RewardEditMenu(this.reward, this.crate).openMenu(player);
            return END_OF_CONVERSATION;
        }
        this.reward.setType(RewardType.COMMAND);
        if (this.reward.getCommands() == null) {
            this.reward.setCommands(Lists.newArrayList());
        }
        this.reward.getCommands().add(input);
        this.menu.openMenu(player);
        player.sendMessage(CC.translate("&aCommand &e" + input + "&e has been added to the reward."));
        return END_OF_CONVERSATION;
    }

    public RewardAddCommandPrompt(Reward reward, Crate crate, Menu menu) {
        this.reward = reward;
        this.crate = crate;
        this.menu = menu;
    }
}

