/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package dev.panda.crates.utils.menu.buttons;

import dev.panda.crates.utils.Callback;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BooleanButton
extends Button {
    private final boolean confirm;
    private final Callback<Boolean> callback;
    private final boolean closeAfterResponse;

    @Override
    public String getName(Player p0) {
        return null;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(CompatibleMaterial.WOOL.getMaterial(), 1, this.confirm ? (short)5 : 14);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.confirm ? "§aConfirm" : "§cCancel");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (this.closeAfterResponse) {
            Menu menu = Menu.currentlyOpenedMenus.get(player.getUniqueId());
            if (menu != null) {
                menu.setClosedByMenu(true);
            }
            player.closeInventory();
        }
        if (this.confirm) {
            player.playSound(player.getLocation(), CompatibleSound.NOTE_PIANO.getSound(), 20.0f, 0.1f);
        } else {
            player.playSound(player.getLocation(), CompatibleSound.DIG_GRAVEL.getSound(), 20.0f, 0.1f);
        }
        this.callback.callback(this.confirm);
    }

    public BooleanButton(boolean confirm, Callback<Boolean> callback, boolean closeAfterResponse) {
        this.confirm = confirm;
        this.callback = callback;
        this.closeAfterResponse = closeAfterResponse;
    }
}

