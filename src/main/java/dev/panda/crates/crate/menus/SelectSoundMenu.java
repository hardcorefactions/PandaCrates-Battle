/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package dev.panda.crates.crate.menus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.StringUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSoundMenu
extends PaginatedMenu {
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "Select Sound";
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Select Sound";
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(4, Button.fromItem(new ItemBuilder(CompatibleMaterial.BED.getMaterial()).name("&cGo Back").build(), other -> new CrateEditMenu(this.crate).openMenu(other)));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        for (Sound sound : Sound.values()) {
            buttons.put(buttons.size(), new SoundButton(sound));
        }
        return buttons;
    }

    public SelectSoundMenu(Crate crate) {
        this.crate = crate;
    }

    public class SoundButton
    extends Button {
        private final Sound sound;

        @Override
        public String getName(Player player) {
            return "&b" + StringUtils.getNiceEnumName(this.sound);
        }

        @Override
        public List<String> getDescription(Player player) {
            return Lists.newArrayList("&8Sound", "", "&9&l" + CC.STRAIGHT_BAR + " &7This sound will be played", "&9&l" + CC.STRAIGHT_BAR + " &7when a player opens the crate.", " ", " &9➥ &bLeft Click &7to set sound.", " &9➥ &bRight Click &7to preview sound.");
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.JUKEBOX;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                player.playSound(player.getLocation(), this.sound, 1.0f, 1.0f);
                SelectSoundMenu.this.crate.setOpenSound(this.sound);
                player.sendMessage(CC.translate("&bThe new sound when a player opens the crate is &f" + StringUtils.getNiceEnumName(this.sound) + "&b."));
                new CrateEditMenu(SelectSoundMenu.this.crate).openMenu(player);
            } else {
                player.playSound(player.getLocation(), this.sound, 1.0f, 1.0f);
            }
        }

        public SoundButton(Sound sound) {
            this.sound = sound;
        }
    }
}

