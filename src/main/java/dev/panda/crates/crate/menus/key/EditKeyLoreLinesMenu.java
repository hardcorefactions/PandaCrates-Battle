/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.menus.key;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.prompt.CrateKeyLoreAddLinePrompt;
import dev.panda.crates.crate.prompt.CrateKeyLoreEditLinePrompt;
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

public class EditKeyLoreLinesMenu
extends Menu {
    private final Crate crate;
    public int selected = 0;

    @Override
    public String getTitle(Player player) {
        return "Edit Key Lore Lines";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(13, new KeyLinesButton(this, this.crate));
        buttons.put(21, Button.fromItem(new ItemBuilder(CompatibleMaterial.WOOL.getMaterial()).data(4).name("&6Edit Line").lore("&8Line Editor", "", "&9&l" + CC.STRAIGHT_BAR + " &7To modify the selected line:", " ", " &9➥ &bShift Right Click &7to remove line.", " &9➥ &bRight Click &7to edit line.").build(), (other, type) -> {
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
            ChatUtils.beginPrompt(other, new CrateKeyLoreAddLinePrompt(this.crate, this.selected));
        }));
        buttons.put(this.size(buttons) - 5, new BackButton(new CrateEditMenu(this.crate)));
        return buttons;
    }

    private void remove(Player player) {
        if (this.crate.getKeyLoreLines().size() == 1) {
            player.sendMessage(CC.RED + "There must be at least 1 line.");
            return;
        }
        this.crate.getKeyLoreLines().remove(this.selected);
        player.sendMessage(CC.RED + "You've removed the line " + this.selected + " from the key lore.");
    }

    private void edit(Player player) {
        ChatUtils.beginPrompt(player, new CrateKeyLoreEditLinePrompt(this.crate, this.selected));
    }

    public EditKeyLoreLinesMenu(Crate crate) {
        this.crate = crate;
    }

    private static class KeyLinesButton
    extends Button {
        private final EditKeyLoreLinesMenu menu;
        private final Crate crate;

        public KeyLinesButton(EditKeyLoreLinesMenu menu, Crate crate) {
            this.menu = menu;
            this.crate = crate;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER).name("&bKey Lore Lines");
            ArrayList<String> list = new ArrayList<>();
            list.add("&8Lore");
            list.add(" ");
            list.add("&9&l" + CC.STRAIGHT_BAR + " &bLeft Click &fto go upwards");
            list.add("&9&l" + CC.STRAIGHT_BAR + " &bRight Click &fto go downwards");
            list.add(" ");
            list.add("&9&l" + CC.STRAIGHT_BAR + " &fLines:");
            list.add(" ");
            if (this.crate.getKeyLoreLines().isEmpty()) {
                list.add("&cNone");
            } else {
                for (int i = 0; i < this.crate.getKeyLoreLines().size(); ++i) {
                    String line = this.crate.getKeyLoreLines().get(i);
                    String prefix = CC.WHITE;
                    if (i == this.menu.selected) {
                        prefix = CC.GREEN + "▸" + CC.WHITE + " ";
                    }
                    list.add(prefix + line);
                }
            }
            list.add(" ");
            list.add("&9&l" + CC.STRAIGHT_BAR + " &fEnd");
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
            if (this.menu.selected > this.crate.getKeyLoreLines().size() - 1) {
                this.menu.selected = 0;
            }
            if (this.menu.selected < 0) {
                this.menu.selected = this.crate.getKeyLoreLines().size() - 1;
            }
            KeyLinesButton.playNeutral(player);
        }
    }
}

