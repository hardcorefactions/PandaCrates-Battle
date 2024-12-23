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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.hologram.EditHologramLinesMenu;
import dev.panda.crates.crate.menus.key.KeySettingsMenu;
import dev.panda.crates.crate.prompt.CrateHologramItemPrompt;
import dev.panda.crates.crate.prompt.CrateRenamePrompt;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.StringUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateEditMenu
extends Menu {
    private final Crate crate;

    public CrateEditMenu(Crate crate) {
        this.crate = crate;
        this.setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        String name = "&bEditing &a" + this.crate.getDisplayName() + "&b...";
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        return name;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 45;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(this.getSlot(2, 1), Button.fromItem(new ItemBuilder(Material.NAME_TAG).name("&9&l【 &b&lDisplay Name &9&l】").lore("&8Change Display Name", "", "&9&l" + CC.STRAIGHT_BAR + " &7Change the display name of the crate.", "", "&9&l▪ &7Status: &f" + this.crate.getDisplayName(), " ", "&b➥ Click to edit.").build(), other -> {
            ChatUtils.beginPrompt(other, new CrateRenamePrompt(this.crate));
            Button.playSuccess(other);
        }));
        buttons.put(this.getSlot(3, 1), Button.fromItem(new ItemBuilder(Material.CHEST).name("&9&l【 &b&lManage Rewards &9&l】").lore("&8Rewards", "", "&9&l" + CC.STRAIGHT_BAR + " &7Change manage the rewards of", "&9&l" + CC.STRAIGHT_BAR + " &7this crate.", " ", "&b➥ Click to manage.").build(), this.crate::openEditRewardsInventory));
        buttons.put(this.getSlot(6, 2), Button.fromItem(new ItemBuilder(this.crate.getKey().getType()).data(this.crate.getKey().getDurability()).name("&9&l【 &b&lKey Settings &9&l】").lore("&8Key Settings", "", "&9&l" + CC.STRAIGHT_BAR + " &7Modify key settings such", "&9&l" + CC.STRAIGHT_BAR + " &7as icon and lore lines.", " ", "&b➥ Click to edit.").build(), other -> new KeySettingsMenu(this.crate).open(other)));
        buttons.put(this.getSlot(4, 2), Button.fromItem(new ItemBuilder(CompatibleMaterial.FIREWORK.getMaterial()).name("&9&l【 &b&lParticle Effect &9&l】").lore("&8Effects", "", "&9&l" + CC.STRAIGHT_BAR + " &7Select the particle and shape that.", "&9&l" + CC.STRAIGHT_BAR + " &7will be displayed around this crate.", " ", "&b➥ Click to select.").build(), other -> new SelectEffectMenu(Crates.getInstance(), this.crate).openMenu(player)));
        buttons.put(this.getSlot(5, 1), Button.fromItem(new ItemBuilder(this.crate.getHologramItem() == null ? new ItemStack(Material.STICK) : this.crate.getHologramItem()).name("&9&l【 &b&lHologram Item &9&l】").lore("&8Hologram Item", "", "&9&l" + CC.STRAIGHT_BAR + " &7Modify the hologram item of", "&9&l" + CC.STRAIGHT_BAR + " &7this crate to a new one.", "", "&9&l▪ &7Status: &f" + StringUtils.capitalize(this.crate.isHologramItemNull() ? "none" : this.crate.getHologramItem().getType().name().toLowerCase().replace("_", " ")), "", "&b➥ Click to select.").build(), other -> {
            ChatUtils.beginPrompt(other, new CrateHologramItemPrompt(this.crate));
            Button.playSuccess(other);
        }));
        buttons.put(this.getSlot(6, 1), Button.fromItem(new ItemBuilder(Material.PAPER).name("&9&l【 &b&lManage Hologram &9&l】").lore("&8Hologram Manager", "", "&9&l" + CC.STRAIGHT_BAR + " &7Click to manage the hologram", "&9&l" + CC.STRAIGHT_BAR + " &7that spawns above the crate.", "", "&b➥ Click to select.").build(), other -> new EditHologramLinesMenu(this.crate).open(other)));
        buttons.put(this.getSlot(5, 2), Button.fromItem(new ItemBuilder(Material.JUKEBOX).name("&9&l【 &b&lOpen Sound &9&l】").lore("&8Open Sound", "", "&9&l" + CC.STRAIGHT_BAR + " &7Click to select the sound that", "&9&l" + CC.STRAIGHT_BAR + " &7is played when a player opens.", "", "&b➥ Click to select.").build(), other -> new SelectSoundMenu(this.crate).openMenu(other)));
        buttons.put(this.getSlot(2, 2), new MinAmountButton());
        buttons.put(this.getSlot(5, 3), Button.fromItem(new ItemBuilder(Material.LEVER).name("&9&l【 &b&lToggle Crate &9&l】").lore("&8Toggle Crate", "", "&9&l" + CC.STRAIGHT_BAR + " &7Toggle if the crate is usable or not", "&9&l" + CC.STRAIGHT_BAR + " &7click to toggle the value.", " ", " " + CC.BLUE + "▸" + " " + (!this.crate.isEnable() ? CC.RED + CC.BOLD : CC.YELLOW) + "Disabled", " " + CC.BLUE + "▸" + " " + (this.crate.isEnable() ? CC.GREEN + CC.BOLD : CC.YELLOW) + "Enabled").build(), other -> {
            this.crate.setEnable(!this.crate.isEnable());
            Button.playNeutral(other);
        }));
        buttons.put(this.getSlot(3, 2), new MaxRewardsButton());
        buttons.put(this.getSlot(3, 3), Button.fromItem(new ItemBuilder(Material.FLINT_AND_STEEL).name("&9&l【 &b&lDelete &9&l】").lore("&8Delete Crate", "", "&9&l" + CC.STRAIGHT_BAR + " &7Click to delete this", "&9&l" + CC.STRAIGHT_BAR + " &7crate or to delete", "&9&l" + CC.STRAIGHT_BAR + " &7the crate location.", "", "&b➥ Click to open.").build(), other -> new CrateDeleteMenu(this.crate).openMenu(other)));
        buttons.put(this.getSlot(4, 1), Button.fromItem(new ItemBuilder(Material.ENDER_CHEST).name("&9&l【 &b&lPreview Rewards &9&l】").lore("&8Preview Rewards", "", "&9&l" + CC.STRAIGHT_BAR + " &7Click to preview all the", "&9&l" + CC.STRAIGHT_BAR + " &7rewards of the crate", "", "&b➥ Click to preview.").build(), other -> new CratePreviewMenu(this.crate).openMenu(other)));
        buttons.put(0, new BackButton(new CrateManageMenu()));
        return buttons;
    }

    public class MinAmountButton
    extends Button {
        @Override
        public String getName(Player player) {
            return "&9&l【 &b&lMinimum Rewards &9&l】";
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Minimum Rewards", " ", "&9&l" + CC.STRAIGHT_BAR + " " + CC.GRAY + "Select the minimum rewards amount", "&9&l" + CC.STRAIGHT_BAR + " " + CC.GRAY + "that can be given of this crate.", " ", "&7[&c&l-&7] &cRight-Click to decrease", "&7[&a&l+&7] &aLeft-Click to increase", " ", "&9&l▪ &7Status: &f" + CrateEditMenu.this.crate.getMinimumReward() + " reward" + (CrateEditMenu.this.crate.getMinimumReward() > 1 ? "s" : ""));
        }

        @Override
        public Material getMaterial(Player player) {
            return CompatibleMaterial.WOOD_BUTTON.getMaterial();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.RIGHT) {
                if (CrateEditMenu.this.crate.getMinimumReward() == 1) {
                    player.sendMessage(CC.translate("&cCrates with a minimum reward of 1 are not allowed."));
                    Button.playFail(player);
                    return;
                }
                Button.playNeutral(player);
                CrateEditMenu.this.crate.setMinimumReward(CrateEditMenu.this.crate.getMinimumReward() - 1);
            } else if (clickType == ClickType.LEFT) {
                CrateEditMenu.this.crate.setMinimumReward(CrateEditMenu.this.crate.getMinimumReward() + 1);
                if (CrateEditMenu.this.crate.getMaximumReward() < CrateEditMenu.this.crate.getMinimumReward()) {
                    CrateEditMenu.this.crate.setMaximumReward(CrateEditMenu.this.crate.getMinimumReward());
                }
                Button.playNeutral(player);
            }
        }
    }

    public class MaxRewardsButton
    extends Button {
        @Override
        public String getName(Player player) {
            return "&9&l【 &b&lMaximum Rewards &9&l】";
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Maximum Rewards", " ", "&9&l" + CC.STRAIGHT_BAR + " " + CC.GRAY + "Select the maximum rewards amount", "&9&l" + CC.STRAIGHT_BAR + " " + CC.GRAY + "that can be given of this crate.", " ", "&7[&c&l-&7] &cRight-Click to decrease", "&7[&a&l+&7] &aLeft-Click to increase", " ", "&9&l▪ &7Status: &f" + CrateEditMenu.this.crate.getMaximumReward() + " reward" + (CrateEditMenu.this.crate.getMaximumReward() > 1 ? "s" : ""));
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.STONE_BUTTON;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.RIGHT) {
                if (CrateEditMenu.this.crate.getMaximumReward() <= CrateEditMenu.this.crate.getMinimumReward()) {
                    player.sendMessage(CC.translate("&cMaximum reward must be greater than or equal to minimum reward."));
                    Button.playFail(player);
                    return;
                }
                MaxRewardsButton.playNeutral(player);
                CrateEditMenu.this.crate.setMaximumReward(CrateEditMenu.this.crate.getMaximumReward() - 1);
            } else if (clickType == ClickType.LEFT) {
                CrateEditMenu.this.crate.setMaximumReward(CrateEditMenu.this.crate.getMaximumReward() + 1);
                MaxRewardsButton.playNeutral(player);
            }
        }
    }
}

