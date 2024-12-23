/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.utils.menu;

import com.google.common.collect.Maps;
import dev.panda.crates.Crates;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class Menu {
    @Setter
    private Method openInventoryMethod;
    private final Map<Integer, Button> buttons = Maps.newConcurrentMap();
    @Setter
    private boolean autoUpdate = false;
    @Setter
    private boolean closedByMenu = false;
    @Setter
    private boolean updateAfterClick = true;
    @Setter
    private boolean placeholder = false;
    @Setter
    private boolean nonCancellingInventory = false;
    @Setter
    private String staticTitle = null;
    public static final Map<String, Menu> currentlyOpenedMenus = new ConcurrentHashMap<>();
    @Setter
    private Inventory inventory;

    private Inventory createInventory(Player player) {
        Inventory inv;
        Map<Integer, Button> invButtons = this.getButtons(player);
        String title = CC.translate(this.getTitle(player));
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        if (this.inventory == null) {
            this.inventory = inv = Bukkit.createInventory(player, this.size(invButtons), title);
        } else {
            inv = this.inventory;
        }
        this.inventory.clear();
        for (Map.Entry<Integer, Button> buttonEntry : invButtons.entrySet()) {
            this.buttons.put(buttonEntry.getKey(), buttonEntry.getValue());
            inv.setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
        }
        if (this.isPlaceholder()) {
            Button placeholder = Button.placeholder(CompatibleMaterial.STAINED_GLASS_PANE.getMaterial(), (byte)15);
            for (int index = 0; index < this.size(invButtons); ++index) {
                if (invButtons.get(index) != null) continue;
                this.buttons.put(index, placeholder);
                inv.setItem(index, placeholder.getButtonItem(player));
            }
        }
        return inv;
    }

    public Menu(String staticTitle) {
        this.staticTitle = staticTitle;
    }

    public void open(Player player) {
        this.openMenu(player);
    }

    public void openMenu(Player player) {
        Menu previousMenu = currentlyOpenedMenus.get(player.getName());
        if (previousMenu != null) {
            previousMenu.onClose(player);
        }
        Inventory inv = this.createInventory(player);
        try {
            player.openInventory(inv);
            currentlyOpenedMenus.put(player.getName(), this);
            this.onOpen(player);
            try {
                Inventory newInv = this.createInventory(player);
                for (int i = 0; i < player.getOpenInventory().getTopInventory().getSize(); ++i) {
                    player.getOpenInventory().getTopInventory().setItem(i, newInv.getItem(i));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Menu cannot be created cuz size is below 9, and equals " + player.getOpenInventory().getTopInventory().getSize());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void update(Player player) {
        this.inventory.clear();
        Map<Integer, Button> invButtons = this.getButtons(player);
        for (Map.Entry<Integer, Button> buttonEntry : invButtons.entrySet()) {
            this.inventory.setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
        }
        if (this.isPlaceholder()) {
            Button placeholder = Button.placeholder(CompatibleMaterial.STAINED_GLASS_PANE.getMaterial(), (byte)15);
            for (int index = 0; index < this.size(invButtons); ++index) {
                if (invButtons.get(index) != null) continue;
                this.inventory.setItem(index, placeholder.getButtonItem(player));
            }
        }
        player.updateInventory();
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue <= highest) continue;
            highest = buttonValue;
        }
        return (int)(Math.ceil((double)(highest + 1) / 9.0) * 9.0);
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public String getTitle(Player player) {
        return this.staticTitle;
    }

    public abstract Map<Integer, Button> getButtons(Player var1);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }

    public void fillBorders(Player player, Button button) {
        int rows = this.inventory.getSize() / 9;
        int columns = this.inventory.getSize() % 9;
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                this.inventory.setItem(this.getSlot(row, column), button.getButtonItem(player));
            }
        }
    }

    public void fillEmpty(Player player, Button button) {
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            if (this.inventory.getItem(i) != null) continue;
            this.inventory.setItem(i, button.getButtonItem(player));
        }
    }

    public Menu() {
    }

    static {
        Bukkit.getServer().getPluginManager().registerEvents(new ButtonListener(), Crates.getInstance());
        Bukkit.getScheduler().runTaskTimerAsynchronously(Crates.getInstance(), new MenuRunnable(), 40L, 10L);
    }
}

