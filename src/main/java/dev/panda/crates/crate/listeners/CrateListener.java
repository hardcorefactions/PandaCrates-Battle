/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.crate.listeners;

import dev.panda.crates.Crates;
import dev.panda.crates.airdrop.Airdrop;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.CratePlaceholder;
import dev.panda.crates.crate.menus.CSGOOpenMenu;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.menus.CratePreviewMenu;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.supplydrop.SupplyDrop;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.RandomUtils;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import dev.panda.crates.utils.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CopyOnWriteArrayList;

public class CrateListener
implements Listener {
    public CrateListener(Crates plugin) {
        if (plugin.isServerLoaded()) {
            Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Inventory inventory = event.getInventory();
        if (player.hasMetadata("closeByRightClick")) {
            player.removeMetadata("closeByRightClick", Crates.getInstance());
            return;
        }
        if (event.getView().getTitle().contains(CC.translate("&eEdit Rewards of "))) {
            String name = event.getView().getTitle().replace(CC.translate("&eEdit Rewards of "), "");
            Crate crate = Crate.getByName(name);
            if (crate == null) {
                return;
            }
            CopyOnWriteArrayList<CratePlaceholder> placeholders = new CopyOnWriteArrayList<>();
            for (int i = 0; i < 54; ++i) {
                MysteryBox box;
                ItemStack item = inventory.getItem(i);
                if (item == null || item.getType() == null || crate.getReward(i) != null || crate.getPlaceholder(i) != null || crate instanceof MysteryBox && ((MysteryBox)crate).getObligatoryReward(i) != null) continue;
                placeholders.add(new CratePlaceholder(item, i));
            }
            crate.getPlaceholders().addAll(placeholders);
            player.sendMessage(CC.translate("&aSuccessfully updated the rewards of &e" + name + "&a!"));
            player.playSound(player.getLocation(), CompatibleSound.LEVEL_UP.getSound(), 0.5f, 0.5f);
            TaskUtil.runAsync(crate::save);
            TaskUtil.runLater(() -> new CrateEditMenu(crate).openMenu(player), 2L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Crate crate = Crate.getCrateByKey(player.getItemInHand());
        Crate clickedCrate = Crate.getByBlock(event.getClickedBlock());
        if (crate != null) {
            event.setCancelled(true);
            if (player.isSneaking() && player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
                new CrateEditMenu(crate).openMenu(player);
                return;
            }
            if (crate instanceof SupplyDrop) {
                return;
            }
            if (crate instanceof Airdrop) {
                player.sendMessage(CC.translate("&cTo use AirDrop you have to put the block on the ground."));
                return;
            }
            if (crate instanceof MysteryBox) {
                crate.openCrate(player);
                return;
            }
            if (crate.getName().equalsIgnoreCase("Giftbox")) {
                crate.consumeKey(player);
                if (player.isSneaking()) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(CC.translate("&cYour inventory is full!"));
                        return;
                    }
                    Reward reward = RandomUtils.getRandomReward(crate.getRewards());
                    player.getInventory().addItem(reward.getItem());
                } else {
                    new CSGOOpenMenu(crate).openMenu(player);
                }
                return;
            }
            if (event.getAction().name().contains("RIGHT_CLICK")) {
                if (crate.isVirtual()) {
                    crate.openCrate(player);
                    return;
                }
                if (clickedCrate == null) {
                    player.sendMessage(CC.RED + "This key cannot be opened freely, you must right click the crate itself.");
                    return;
                }
                if (clickedCrate.equals(crate)) {
                    clickedCrate.openCrate(player);
                } else {
                    new CratePreviewMenu(clickedCrate).openMenu(player);
                }
                return;
            }
        }
        if (event.getAction().name().contains("LEFT_CLICK") && (crate != null || clickedCrate != null)) {
            new CratePreviewMenu(clickedCrate != null ? clickedCrate : crate).openMenu(player);
        }
    }

    @EventHandler
    public void onSetChestWithBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasMetadata("breakBlockToSetChest")) {
            return;
        }
        Crate crate = (Crate) player.getMetadata("breakBlockToSetChest").get(0).value();
        if (crate == null) {
            player.sendMessage(CC.RED + "Something went wrong.");
            return;
        }
        event.setCancelled(true);
        crate.handleSetChest(player, event.getBlock().getLocation());
        player.removeMetadata("breakBlockToSetChest", Crates.getInstance());
    }
}

