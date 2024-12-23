/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.menu.pagination;

import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PaginatedMenu
extends Menu {
    private int page = 1;

    public PaginatedMenu() {
        this.setUpdateAfterClick(false);
    }

    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player) + " - " + this.page + "/" + this.getPages(player);
    }

    public final void modPage(Player player, int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.openMenu(player);
    }

    public int getPages(Player player) {
        int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int)Math.ceil((double)buttonAmount / (double)this.getMaxItemsPerPage(player));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int)((double)(this.page - 1) * (double)this.getMaxItemsPerPage(player));
        int maxIndex = (int)((double)this.page * (double)this.getMaxItemsPerPage(player));
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));
        for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind < minIndex || ind >= maxIndex) continue;
            buttons.put(ind - ((int) ((double) this.getMaxItemsPerPage(player) * (double) (this.page - 1)) - 9), entry.getValue());
        }
        Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            buttons.putAll(global);
        }
        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(Player var1);

    public abstract Map<Integer, Button> getAllPagesButtons(Player var1);

}

