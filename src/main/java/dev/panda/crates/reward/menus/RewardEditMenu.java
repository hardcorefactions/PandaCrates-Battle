/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.reward.menus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateBroadcastMenu;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.promt.RewardAddCommandPrompt;
import dev.panda.crates.reward.promt.RewardSetChancePrompt;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardEditMenu
extends Menu {
    private final Reward reward;
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "&eEditing reward...";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    @Override
    public void onClose(Player player) {
        if (player.hasMetadata("closeByRightClick")) {
            player.removeMetadata("closeByRightClick", Crates.getInstance());
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(this.getSlot(0, 0), Button.fromItem(new ItemBuilder(CompatibleMaterial.BED.getMaterial()).name("&cGo Back").lore("", "&7Click to go back to the reward edit menu.").build(), other -> {
            other.closeInventory();
            this.crate.openEditRewardsInventory(other);
        }));
        buttons.put(this.getSlot(2, 1), Button.fromItem(new ItemBuilder(CompatibleMaterial.EXP_BOTTLE.getMaterial()).name("&bChance").lore("&8Chance Percentage", "", CC.CR_STRAIGHT_BAR + "This will modify the chance of getting", CC.CR_STRAIGHT_BAR + "this reward upon opening the crate.", "", CC.C_SQUARE + "&7Status: &a" + this.reward.getChance() + "%", "", CC.A_LEFT_ARROW + "Click to edit.").build(), other -> ChatUtils.beginPrompt(other, new RewardSetChancePrompt(this.reward, this.crate))));
        if (this.crate instanceof MysteryBox) {
            MysteryBox box = (MysteryBox)this.crate;
            buttons.put(this.getSlot(4, 2), Button.fromItem(new ItemBuilder(Material.ANVIL).name(box.getObligatoryRewards().contains(this.reward) ? "&aObligatory" : "&cObligatory").lore("", (box.getObligatoryRewards().contains(this.reward) ? "&cClick to unset" : "&aClick to set") + " &7as an obligatory reward").build(), other -> {
                if (box.getObligatoryRewards().contains(this.reward)) {
                    box.getObligatoryRewards().remove(this.reward);
                    box.getRewards().add(this.reward);
                } else {
                    box.getObligatoryRewards().add(this.reward);
                    box.getRewards().remove(this.reward);
                }
                Button.playNeutral(other);
            }));
        }
        buttons.put(this.getSlot(6, 1), new CommandButtons());
        buttons.put(this.getSlot(4, 1), Button.fromItem(new ItemBuilder(Material.PAPER).name("&bBroadcast Lines").lore("&8Lines", "", CC.CR_STRAIGHT_BAR + "By adding lines here upon receiving", CC.CR_STRAIGHT_BAR + "this reward, the added lines will be", CC.CR_STRAIGHT_BAR + "broadcasted to the global chat.", "", CC.C_SQUARE + "&7Use &b{player} &7for the player name", CC.C_SQUARE + "&7Use &b{display} &7for the crate display", "", CC.A_LEFT_ARROW + "Click to manage.").build(), other -> new CrateBroadcastMenu(this.crate, this.reward).openMenu(other)));
        return buttons;
    }

    public RewardEditMenu(Reward reward, Crate crate) {
        this.reward = reward;
        this.crate = crate;
    }

    public class CommandButtons
    extends Button {
        @Override
        public String getName(Player player) {
            return "&bCommands";
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Command Reward", "", CC.CR_STRAIGHT_BAR + "Instead of a normal item reward this", CC.CR_STRAIGHT_BAR + "will make the console execute a", CC.CR_STRAIGHT_BAR + "command.", "", CC.C_SQUARE + "&7Use &b{player} &7for the player name", CC.C_SQUARE + "&7Use &b{display} &7for the crate display", CC.C_SQUARE + "&7Use &b{reward} &7for the reward name", "", CC.C_LEFT_ARROW + "&bLeft Click &7to add a command", CC.C_LEFT_ARROW + "&bRight Click &7to manage commands", "", CC.C_SQUARE + "&7Current: &a" + (RewardEditMenu.this.reward.getCommands() == null || RewardEditMenu.this.reward.getCommands().isEmpty() ? "None" : Arrays.toString(RewardEditMenu.this.reward.getCommands().toArray())));
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.BOOK;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                ChatUtils.beginPrompt(player, new RewardAddCommandPrompt(RewardEditMenu.this.reward, RewardEditMenu.this.crate, new RewardEditMenu(RewardEditMenu.this.reward, RewardEditMenu.this.crate)));
            } else {
                new RewardCommandsMenu(RewardEditMenu.this.reward, RewardEditMenu.this.crate).openMenu(player);
            }
        }
    }
}

