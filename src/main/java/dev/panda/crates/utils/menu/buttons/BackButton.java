/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package dev.panda.crates.utils.menu.buttons;

import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BackButton
extends Button {
    private final Menu back;

    @Override
    public Material getMaterial(Player player) {
        return Material.ARROW;
    }

    @Override
    public String getName(Player player) {
        return "§cGo back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }

    public BackButton(Menu back) {
        this.back = back;
    }
}

