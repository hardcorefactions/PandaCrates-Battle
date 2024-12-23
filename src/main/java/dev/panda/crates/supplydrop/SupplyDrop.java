/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.supplydrop;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SupplyDrop extends Crate {

    public SupplyDrop() {
        super("SupplyDrop");
        SupplyDrop.getCrates().put("SupplyDrop", this);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(this.getDisplayName() + " SupplyDrop", "&7", "&fLeft click &7for preview rewards", "&7", "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(Material.DROPPER).name("&4&lSupplyDrop").lore("&7Purchasable at &fstore.battle.rip&7.", "").build();
    }
}

