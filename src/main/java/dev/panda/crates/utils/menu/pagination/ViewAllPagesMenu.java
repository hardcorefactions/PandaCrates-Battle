/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.NonNull
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.menu.pagination;

import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BackButton;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu
extends Menu {
    @NonNull
    PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return CC.YELLOW + "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(this.menu));
        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); ++i) {
            buttons.put(index++, new JumpToPageButton(i, this.menu));
            if ((index - 8) % 9 != 0) continue;
            index += 2;
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
        this.menu = menu;
    }

    @NonNull
    public PaginatedMenu getMenu() {
        return this.menu;
    }
}

