/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 */
package dev.panda.crates.utils.menu.pagination;

import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class PageButton
extends Button {
    private final int mod;
    private final PaginatedMenu menu;

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            Button.playNeutral(player);
        } else if (this.hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
        } else {
            Button.playFail(player);
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }

    @Override
    public String getName(Player player) {
        if (!this.hasNext(player)) {
            return this.mod > 0 ? "§7Last page" : "§7First page";
        }
        String str = "(§e" + (this.menu.getPage() + this.mod) + "/§e" + this.menu.getPages(player) + "§a)";
        return this.mod > 0 ? "§a⟶" : "§c⟵";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public byte getDamageValue(Player player) {
        return (byte)(this.hasNext(player) ? 11 : 7);
    }

    @Override
    public Material getMaterial(Player player) {
        return CompatibleMaterial.CARPET.getMaterial();
    }

    public PageButton(int mod, PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }
}

