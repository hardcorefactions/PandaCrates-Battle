/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package dev.panda.crates.reward.menus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.RewardType;
import dev.panda.crates.reward.promt.RewardAddCommandPrompt;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardCommandsMenu
extends Menu {
    private final Reward reward;
    private final Crate crate;

    public RewardCommandsMenu(Reward reward, Crate crate) {
        this.reward = reward;
        this.crate = crate;
        this.setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return ChatColor.GREEN + "Manage Commands";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(this.getSlot(0, 0), Button.fromItem(new ItemBuilder(CompatibleMaterial.BED.getMaterial()).name("&cGo Back").lore("&CGo back to the reward", "&cedit menu.").build(), other -> {
            if (this.reward.getCommands().isEmpty()) {
                this.reward.setType(RewardType.ITEMS);
            }
            new RewardEditMenu(this.reward, this.crate).openMenu(other);
        }));
        if (this.reward.getCommands().isEmpty()) {
            buttons.put(this.getSlot(4, 0), Button.fromItem(new ItemBuilder(Material.PAPER).name("&bAdd Command").lore("&8Command Addition", "", CC.CR_STRAIGHT_BAR + "This will add a command to execute", CC.CR_STRAIGHT_BAR + "when this reward is given to a player.", "", CC.A_LEFT_ARROW + "Click to add command.").build(), other -> ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(this.reward, this.crate, new RewardCommandsMenu(this.reward, this.crate)))));
        }
        int slot = 9;
        for (String command : this.reward.getCommands()) {
            buttons.put(slot, new CommandButton(command));
        }
        return buttons;
    }

    public class CommandButton
    extends Button {
        private final String command;

        @Override
        public String getName(Player player) {
            return ChatColor.AQUA + this.command;
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Command", "", CC.CR_STRAIGHT_BAR + "To modify the command, use:", "", CC.C_LEFT_ARROW + "Left Click &7to edit command", CC.C_LEFT_ARROW + "Right Click &7to remove command");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            RewardCommandsMenu.this.reward.getCommands().remove(this.command);
            if (clickType == ClickType.LEFT) {
                ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(RewardCommandsMenu.this.reward, RewardCommandsMenu.this.crate, new RewardCommandsMenu(RewardCommandsMenu.this.reward, RewardCommandsMenu.this.crate)));
                CommandButton.playSuccess(player);
            } else if (clickType == ClickType.RIGHT) {
                Button.playNeutral(player);
            }
        }

        public CommandButton(String command) {
            this.command = command;
        }
    }
}

