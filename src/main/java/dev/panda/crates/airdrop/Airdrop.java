/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.airdrop;

import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.compatibility.particle.ParticleType;
import dev.panda.crates.utils.item.ItemBuilder;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Airdrop extends Crate {
    public Airdrop() {
        super("Airdrop");
        Airdrop.getCrates().put("Airdrop", this);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(this.getDisplayName() + " Airdrop", "&7", "&fLeft click &7for preview rewards", "&7", "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(Material.CHEST).name("&6&lAirdrop").lore("&7Purchasable at &fstore.battle.rip&7.", "", "&ePlace this down to designate a &6&lLocation &efor the &6&lLoot&e!").build();
    }

    public void startAnimation(Block block, Player player) {
        this.createFlameRings(block.getLocation().add(0.0, 0.0, 0.5));
        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), true));
    }

    private void createFlameRings(Location location) {
        double alpha = 0.0;
        for (int count = 0; count < 50; ++count) {
            Location firstLocation = location.clone().add(Math.cos(alpha += 0.19634954084936207), Math.sin(alpha) + 1.0, Math.sin(alpha));
            Location secondLocation = location.clone().add(Math.cos(alpha + Math.PI), Math.sin(alpha) + 1.0, Math.sin(alpha + Math.PI));
            ParticleType.of("FLAME").spawn(firstLocation.getWorld(), firstLocation, 10, 0.0, 0.0, 0.0, 10.0);
            ParticleType.of("FLAME").spawn(firstLocation.getWorld(), secondLocation, 10, 0.0, 0.0, 0.0, 10.0);
        }
    }
}

