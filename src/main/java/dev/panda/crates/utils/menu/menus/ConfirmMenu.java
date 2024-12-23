/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils.menu.menus;

import dev.panda.crates.utils.Callback;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BooleanButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfirmMenu
extends Menu {
    private final String title;
    private final Callback<Boolean> response;
    private final boolean closeAfterResponse;
    private final Button[] centerButtons;

    public ConfirmMenu(String title, Callback<Boolean> response, boolean closeAfter, Button ... centerButtons) {
        this.title = title;
        this.response = response;
        this.closeAfterResponse = closeAfter;
        this.centerButtons = centerButtons;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                buttons.put(this.getSlot(x, y), new BooleanButton(true, this.response, this.closeAfterResponse));
                buttons.put(this.getSlot(8 - x, y), new BooleanButton(false, this.response, this.closeAfterResponse));
            }
        }
        if (this.centerButtons != null) {
            for (int i = 0; i < this.centerButtons.length; ++i) {
                if (this.centerButtons[i] == null) continue;
                buttons.put(this.getSlot(13, i), this.centerButtons[i]);
            }
        }
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return this.title;
    }
}

