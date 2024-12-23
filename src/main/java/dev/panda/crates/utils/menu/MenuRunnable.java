/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuRunnable
implements Runnable {
    @Override
    public void run() {
        Menu.currentlyOpenedMenus.forEach((name, menu) -> {
            Player player = Bukkit.getPlayer(name);
            if (player != null) {
                if (menu.isAutoUpdate()) {
                    menu.update(player);
                }
            } else {
                Menu.currentlyOpenedMenus.remove(name);
            }
        });
    }
}

