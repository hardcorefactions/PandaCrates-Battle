/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.menus;

import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.menus.ConfirmMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CrateDeleteMenu
extends Menu {
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "Crate Deletion";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(0, Button.fromItem(new ItemBuilder(CompatibleMaterial.BED.getMaterial()).name("&cGo Back").build(), other -> new CrateEditMenu(this.crate).openMenu(other)));
        buttons.put(this.getSlot(3, 1), Button.fromItem(new ItemBuilder(Material.FLINT_AND_STEEL).name("&9&l【 &b&lDelete Crate &9&l】").lore("&8Deletion", "", CC.CR_STRAIGHT_BAR + "This will permanently delete your", CC.CR_STRAIGHT_BAR + "crate and it won't be reversible.", "", CC.A_LEFT_ARROW + CC.RED + "Click to delete crate.").build(), other -> new ConfirmMenu("Delete Crate?", bool -> {
            if (bool) {
                this.crate.delete();
                other.sendMessage(CC.RED + "You've permanently removed the crate.");
            }
        }, true).open(other)));
        buttons.put(this.getSlot(5, 1), Button.fromItem(new ItemBuilder(Material.CHEST).name("&9&l【 &b&lDelete Chest &9&l】").lore("&8Chest Deletion", "", CC.CR_STRAIGHT_BAR + "This will permanently delete the", CC.CR_STRAIGHT_BAR + "chest of the crate and it won't", CC.CR_STRAIGHT_BAR + "be reversible.", "", CC.A_LEFT_ARROW + CC.RED + "Click to delete the chest.").build(), other -> {
            this.crate.destroyHolograms();
            if (this.crate.getEffect() != null) {
                this.crate.getEffect().stop();
            }
            this.crate.setChestLocation(null);
            player.sendMessage(CC.translate("&aSuccessfully removed chest location."));
            other.closeInventory();
        }));
        return buttons;
    }

    public CrateDeleteMenu(Crate crate) {
        this.crate = crate;
    }
}

