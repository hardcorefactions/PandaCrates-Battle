/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.menus.key;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.prompt.CrateKeyItemPrompt;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KeySettingsMenu
extends Menu {
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "Key Settings";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(new CrateEditMenu(this.crate)));
        buttons.put(11, Button.fromItem(new ItemBuilder(this.crate.getKey().getType()).data(this.crate.getKey().getDurability()).name("&9&l【 &b&lChange Key &9&l】").lore("&8Icon Change", "", "&9&l" + CC.STRAIGHT_BAR + " &7Change the key of this", "&9&l" + CC.STRAIGHT_BAR + " &7crate.", " ", "&b➥ Click to edit.").build(), other -> {
            ChatUtils.beginPrompt(other, new CrateKeyItemPrompt(this.crate));
            Button.playSuccess(other);
        }));
        buttons.put(13, Button.fromItem(new ItemBuilder(Material.PAPER).name("&9&l【 &b&lKey Lore &9&l】").lore("&8Lore", "", "&9&l" + CC.STRAIGHT_BAR + " &7Click to manage the lore", "&9&l" + CC.STRAIGHT_BAR + " &7of this key.", " ", "&b➥ Click to manage.").build(), other -> new EditKeyLoreLinesMenu(this.crate).open(other)));
        buttons.put(15, Button.fromItem(new ItemBuilder(Material.DAYLIGHT_DETECTOR).name("&9&l【 &b&lVirtual Mode &9&l】").lore("&8Virtual Mode", "", "&9&l" + CC.STRAIGHT_BAR + " &7Toggle if the key can be opened", "&9&l" + CC.STRAIGHT_BAR + " &7without clicking on the crate.", "", " " + CC.BLUE + "▸" + " " + (!this.crate.isVirtual() ? CC.RED + CC.BOLD : CC.YELLOW) + "Disabled", " " + CC.BLUE + "▸" + " " + (this.crate.isVirtual() ? CC.GREEN + CC.BOLD : CC.YELLOW) + "Enabled").build(), other -> {
            this.crate.setVirtual(!this.crate.isVirtual());
            other.sendMessage((this.crate.isVirtual() ? CC.GREEN : CC.GRAY) + "You toggled virtual mode for crate " + this.crate.getDisplayName() + CC.GREEN + ".");
            Button.playSuccess(other);
        }));
        return buttons;
    }

    public KeySettingsMenu(Crate crate) {
        this.crate = crate;
    }
}

