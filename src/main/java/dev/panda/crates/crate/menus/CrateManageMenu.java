/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.menus;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.StringUtils;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrateManageMenu
extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Manage Crates";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 45;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        int slot = 0;
        for (Crate crate : Crate.getCrates().values()) {
            buttons.put(slot++, new CrateButton(crate));
        }
        return buttons;
    }

    private static class CrateButton
    extends Button {
        private final Crate crate;

        public CrateButton(Crate crate) {
            this.crate = crate;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.crate.getChestLocation() == null ? Material.CHEST : this.crate.getChestLocation().getBlock().getType()).data(this.crate.getChestLocation() == null ? 0 : (int)this.crate.getChestLocation().getBlock().getData()).name(this.crate.getDisplayName() + " Crate").lore("&8Crate", "", CC.CR_STRAIGHT_BAR + "This will allow you to edit", CC.CR_STRAIGHT_BAR + "and manage this crate.", "", CC.CR_STRAIGHT_BAR + "Information:", " " + CC.C_SQUARE + "Effect: &f" + (this.crate.getEffect() == null ? CC.RED + "None" : StringUtils.capitalizeFully(this.crate.getEffect().getParticle().getName())), " " + CC.C_SQUARE + "Open Sound: &f" + (this.crate.getOpenSound() == null ? CC.RED + "None" : StringUtils.getNiceEnumName(this.crate.getOpenSound())), " " + CC.C_SQUARE + "Maximum Rewards: &f" + this.crate.getMaximumReward() + " rewards", " " + CC.C_SQUARE + "Minimum Rewards: &f" + this.crate.getMinimumReward() + " rewards", "", CC.A_LEFT_ARROW + "Click to manage.").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            CrateButton.playSuccess(player);
            new CrateEditMenu(this.crate).open(player);
        }
    }
}

