/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

public class ButtonListener
implements Listener {
    @EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
    public void onButtonPress(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Menu openMenu = Menu.currentlyOpenedMenus.get(player.getName());
        if (openMenu != null) {
            if (event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER && event.getClick() == ClickType.DOUBLE_CLICK) {
                event.setCancelled(true);
                return;
            }
            if (event.getSlot() != event.getRawSlot()) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.NUMBER_KEY || event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP || event.getClick() == ClickType.DOUBLE_CLICK) {
                    event.setCancelled(true);
                    if (openMenu.isNonCancellingInventory() && event.getCurrentItem() != null) {
                        player.getOpenInventory().getTopInventory().addItem(event.getCurrentItem());
                        event.setCurrentItem(null);
                    }
                }
                return;
            }
            if (openMenu.getButtons().containsKey(event.getSlot())) {
                Menu newMenu;
                Button button = openMenu.getButtons().get(event.getSlot());
                boolean cancel = button.shouldCancel(player, event.getSlot(), event.getClick());
                if (!(cancel || event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null) {
                        player.getInventory().addItem(event.getCurrentItem());
                    }
                } else {
                    event.setCancelled(cancel);
                }
                button.clicked(player, event.getSlot(), event.getClick());
                button.clicked(event);
                if (Menu.currentlyOpenedMenus.containsKey(player.getName()) && (newMenu = Menu.currentlyOpenedMenus.get(player.getName())) == openMenu && newMenu.isUpdateAfterClick()) {
                    newMenu.update(player);
                }
                if (event.isCancelled()) {
                    player.updateInventory();
                }
            } else if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.NUMBER_KEY || event.getClick() == ClickType.DROP || event.getClick() == ClickType.CONTROL_DROP || event.getClick() == ClickType.DOUBLE_CLICK) {
                event.setCancelled(true);
                if (openMenu.isNonCancellingInventory() && event.getCurrentItem() != null) {
                    player.getOpenInventory().getTopInventory().addItem(event.getCurrentItem());
                    event.setCurrentItem(null);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Menu openMenu = Menu.currentlyOpenedMenus.get(player.getName());
        if (openMenu != null) {
            openMenu.onClose(player);
            Menu.currentlyOpenedMenus.remove(player.getName());
        }
    }
}

