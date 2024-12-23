/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.menus.hologram;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.prompt.CrateHologramAddLinePrompt;
import dev.panda.crates.crate.prompt.CrateHologramEditLinePrompt;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditHologramLinesMenu
extends Menu {
    private final Crate crate;
    public int selected = 0;

    @Override
    public String getTitle(Player player) {
        return "Edit Hologram Lines";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(13, new HologramLinesButton(this, this.crate));
        buttons.put(21, Button.fromItem(new ItemBuilder(CompatibleMaterial.WOOL.getMaterial()).data(4).name("&eEdit Line").lore("&8Line Editor", "", "&9&l" + CC.STRAIGHT_BAR + " &7To modify the selected line:", " ", " &9➥ &bShift Right Click &7to remove line.", " &9➥ &bRight Click &7to edit line.").build(), (other, type) -> {
            if (!type.isRightClick()) {
                return;
            }
            Button.playSuccess(player);
            if (type.isShiftClick()) {
                this.remove(player);
                return;
            }
            this.edit(player);
        }));
        buttons.put(23, Button.fromItem(new ItemBuilder(CompatibleMaterial.WOOL.getMaterial()).data(5).name(CC.GREEN + "Add Line").lore("&8Line Editor", "", "&9&l" + CC.STRAIGHT_BAR + " &7This will add a new line below", "&9&l" + CC.STRAIGHT_BAR + " &7the line you selected.", " ", " &9➥ &bClick &7to add line.").build(), other -> {
            Button.playSuccess(player);
            ChatUtils.beginPrompt(other, new CrateHologramAddLinePrompt(this.crate, this.selected));
        }));
        buttons.put(this.size(buttons) - 5, new BackButton(new CrateEditMenu(this.crate)));
        return buttons;
    }

    private void remove(Player player) {
        if (this.crate.getHologramLines().isEmpty()) {
            player.sendMessage(CC.RED + "There are no more lines.");
            return;
        }
        this.crate.getHologramLines().remove(this.selected);
        this.crate.respawnHolograms();
        player.sendMessage(CC.RED + "You've removed the line " + this.selected + " from the hologram.");
    }

    private void edit(Player player) {
        ChatUtils.beginPrompt(player, new CrateHologramEditLinePrompt(this.crate, this.selected));
    }

    public EditHologramLinesMenu(Crate crate) {
        this.crate = crate;
    }

    private static class HologramLinesButton
    extends Button {
        private final EditHologramLinesMenu menu;
        private final Crate crate;

        public HologramLinesButton(EditHologramLinesMenu menu, Crate crate) {
            this.menu = menu;
            this.crate = crate;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER).name(CC.YELLOW + "Hologram Lines");
            ArrayList<String> list = new ArrayList<>();
            list.add(CC.CHAT_BAR);
            list.add("&7Current Lines:");
            list.add(" ");
            if (this.crate.getHologramLines().isEmpty()) {
                list.add("&cNone");
            } else {
                for (int i = 0; i < this.crate.getHologramLines().size(); ++i) {
                    String line = this.crate.getHologramLines().get(i);
                    String prefix = CC.WHITE;
                    if (i == this.menu.selected) {
                        prefix = CC.GREEN + "▸" + CC.WHITE + " ";
                    }
                    list.add(prefix + line);
                }
            }
            list.add(" ");
            list.add("&9&l" + CC.STRAIGHT_BAR + " &7End");
            builder.lore(list);
            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType type) {
            if (type == ClickType.LEFT) {
                ++this.menu.selected;
            } else if (type == ClickType.RIGHT) {
                --this.menu.selected;
            }
            if (this.menu.selected > this.crate.getHologramLines().size() - 1) {
                this.menu.selected = 0;
            }
            if (this.menu.selected < 0) {
                this.menu.selected = this.crate.getHologramLines().size() - 1;
            }
            HologramLinesButton.playNeutral(player);
        }
    }
}

