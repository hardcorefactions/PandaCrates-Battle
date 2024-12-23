/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.menus;

import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.CratePlaceholder;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CratePreviewMenu
extends Menu {
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        String name = this.crate.getName();
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        return ChatColor.GOLD + "Preview " + name;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        for (CratePlaceholder placeholder : this.crate.getPlaceholders()) {
            if (placeholder.getItem() == null) continue;
            buttons.put(placeholder.getSlot(), Button.fromItem(placeholder.getPreview()));
        }
        this.crate.getRewards().stream().filter(reward -> reward.getRealItem() != null).forEach(reward -> buttons.put(reward.getSlot(), Button.fromItem(reward.getItem())));
        if (this.crate instanceof MysteryBox) {
            ((MysteryBox)this.crate).getObligatoryRewards().forEach(reward -> buttons.put(reward.getSlot(), Button.fromItem(reward.getItem())));
        }
        return buttons;
    }

    public CratePreviewMenu(Crate crate) {
        this.crate = crate;
    }
}

