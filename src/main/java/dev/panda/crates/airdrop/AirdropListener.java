/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Skull
 *  org.bukkit.entity.FallingBlock
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package dev.panda.crates.airdrop;

import dev.panda.crates.Crates;
import dev.panda.crates.airdrop.Airdrop;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.FireworkUtil;
import dev.panda.crates.utils.ItemUtils;
import dev.panda.crates.utils.LocationUtil;
import dev.panda.crates.utils.RandomUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import dev.panda.crates.utils.task.TaskUtil;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AirdropListener
implements Listener {
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        if (!ItemUtils.isAirDrop(item)) {
            return;
        }
        Airdrop airdrop = (Airdrop)Crate.getByName("Airdrop");
        if (!airdrop.isEnable()) {
            player.sendMessage(CC.translate("&cAirdrop is currently disabled."));
            event.setCancelled(true);
            player.playSound(player.getLocation(), CompatibleSound.ANVIL_BREAK.getSound(), 1.0f, 1.0f);
            return;
        }
        Location location = event.getBlock().getLocation().clone().add(0.0, 25.0, 0.0);
        if (location.getBlock() != null && location.getBlock().getType() != Material.AIR) {
            player.sendMessage(CC.translate("&cYou cannot place an &6&lAirdrop&c there."));
            player.sendMessage(CC.translate("&cGo out in the open air to deploy the &6&lAirdrop&c."));
            player.playSound(player.getLocation(), CompatibleSound.VILLAGER_NO.getSound(), 1.0f, 1.0f);
            event.setCancelled(true);
            return;
        }
        FallingBlock block = player.getLocation().getWorld().spawnFallingBlock(location, Material.SNOW_BLOCK, (byte)3);
        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), player.getName()));
        event.setCancelled(true);
        FireworkUtil.launchFirework(event.getBlock().getLocation());
        ItemUtils.consume(player);
    }

    @EventHandler
    public void onDeath(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof FallingBlock)) {
            return;
        }
        FallingBlock fallingBlock = (FallingBlock)event.getEntity();
        if (!fallingBlock.hasMetadata("airdrop")) {
            return;
        }
        Airdrop airdrop = (Airdrop)Crate.getByName("Airdrop");
        Player player = Bukkit.getPlayer(fallingBlock.getMetadata("airdrop").get(0).asString());
        fallingBlock.setDropItem(false);
        final Block block = event.getBlock();
        new BukkitRunnable(){

            public void run() {
                block.setType(CompatibleMaterial.HUMAN_SKULL.getMaterial());
                Skull skull = (Skull)block.getState();
                skull.setOwner("conhost");
                skull.update();
            }
        }.runTaskLater(Crates.getInstance(), 2L);
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            new BukkitRunnable(){

                public void run() {
                    online.sendBlockChange(block.getLocation(), Material.CHEST, (byte)0);
                }
            }.runTaskLater(Crates.getInstance(), 2L);
        }
        block.setMetadata("airdrop", new FixedMetadataValue(Crates.getInstance(), player.getName()));
        TaskUtil.runAsync(() -> airdrop.startAnimation(block, player));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) {
            return;
        }
        Block block = event.getClickedBlock();
        if (!block.hasMetadata("airdrop")) {
            return;
        }
        event.setCancelled(true);
        if (block.hasMetadata("airdropOpen")) {
            return;
        }
        if (block.getType() != Material.DROPPER) {
            Inventory inventory = Bukkit.createInventory(null, 9, "Airdrop");
            ArrayList<Reward> rewardList = new ArrayList<>(Airdrop.getByName("Airdrop").getRewards());
            Collections.shuffle(rewardList);
            for (int i = 0; i < 9; ++i) {
                Reward randomReward = RandomUtils.getRandomReward(rewardList);
                inventory.setItem(i, randomReward.getItem());
                if (randomReward.getBroadcast().isEmpty()) continue;
                randomReward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
            }
            player.openInventory(inventory);
        }
        block.setMetadata("airdropOpen", new FixedMetadataValue(Crates.getInstance(), true));
        player.setMetadata("airdropOpen", new FixedMetadataValue(Crates.getInstance(), LocationUtil.parseLocation(block.getLocation())));
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (!player.hasMetadata("airdropOpen")) {
            return;
        }
        if (!event.getView().getTitle().equals("Airdrop")) {
            return;
        }
        Inventory inventory = event.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (item == null) continue;
            player.getLocation().getWorld().dropItem(player.getLocation(), item);
        }
        Location location = LocationUtil.convertLocation(player.getMetadata("airdropOpen").get(0).asString());
        Block block = location.getBlock();
        block.setType(Material.AIR);
        player.removeMetadata("airdropOpen", Crates.getInstance());
        block.removeMetadata("airdrop", Crates.getInstance());
    }
}

