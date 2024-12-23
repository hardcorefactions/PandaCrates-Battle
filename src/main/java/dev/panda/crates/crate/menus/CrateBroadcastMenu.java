/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package dev.panda.crates.crate.menus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.prompt.CrateBroadCastPrompt;
import dev.panda.crates.crate.prompt.CrateEditLinePrompt;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.menus.RewardEditMenu;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CrateBroadcastMenu
extends Menu {
    private final Crate crate;
    private final Reward reward;

    public CrateBroadcastMenu(Crate crate, Reward reward) {
        this.crate = crate;
        this.reward = reward;
        this.setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return "&eBroadcast Lines";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(0, Button.fromItem(new ItemBuilder(CompatibleMaterial.BED.getMaterial()).name("&cGo Back").build(), other -> new RewardEditMenu(this.reward, this.crate).openMenu(other)));
        buttons.put(4, Button.fromItem(new ItemBuilder(Material.BOOK).name("&aAdd new lines").build(), other -> ChatUtils.beginPrompt(other, new CrateBroadCastPrompt(this.crate, this.reward))));
        buttons.put(8, Button.fromItem(new ItemBuilder(Material.JUKEBOX).name("&bPreview Broadcast").build(), other -> {
            player.closeInventory();
            LinkedList<String> lines = this.reward.getBroadcast();
            if (lines.isEmpty()) {
                other.sendMessage("&cNo lines!");
            } else {
                lines.forEach(line -> other.sendMessage(line.replace("{player}", other.getName()).replace("{display}", this.crate.getDisplayName())));
            }
        }));
        int i = 9;
        int index = 0;
        for (String line : this.reward.getBroadcast()) {
            buttons.put(i++, new BroadcastLineButton(line, index++));
        }
        return buttons;
    }

    public class BroadcastLineButton
    extends Button {
        private final String line;
        private final int index;

        @Override
        public String getName(Player player) {
            return this.line;
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Broadcast Line", "", CC.C_LEFT_ARROW + "Right Click &7to delete.", CC.C_LEFT_ARROW + "Left Click &7to edit.");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.PAPER;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.RIGHT) {
                CrateBroadcastMenu.this.reward.getBroadcast().remove(this.line);
            } else if (clickType == ClickType.LEFT) {
                ChatUtils.beginPrompt(player, new CrateEditLinePrompt(CrateBroadcastMenu.this.crate, CrateBroadcastMenu.this.reward, this.index));
            }
        }

        public BroadcastLineButton(String line, int index) {
            this.line = line;
            this.index = index;
        }
    }
}

