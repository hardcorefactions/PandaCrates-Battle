/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.menus.types;

import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.*;

public class RulettaMenu
extends Menu {
    private final Crate crate;
    private final List<Reward> rewards;

    public RulettaMenu(Crate crate) {
        this.crate = crate;
        this.rewards = crate.getRewards();
        List<Reward> rotatingRewards = new ArrayList<>(this.rewards);
        int currentPosition = 0;
    }

    @Override
    public String getTitle(Player player) {
        return this.crate.getDisplayName() + " Crate";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        int centerSlot = 4;
        int radius = 5;
        for (int i = 0; i < this.rewards.size(); ++i) {
            int slot = this.getPositionOnCircle(centerSlot, radius, i, this.rewards.size());
            Reward reward = this.rewards.get(i);
            buttons.put(slot, Button.fromItem(reward.getItem()));
        }
        return buttons;
    }

    private int getPositionOnCircle(int centerSlot, int radius, int index, int totalItems) {
        double increment = Math.PI * 2 / (double)totalItems;
        double angle = (double)index * increment;
        int x = centerSlot + (int)((double)radius * Math.cos(angle));
        int z = centerSlot + (int)((double)radius * Math.sin(angle));
        return 9 * x + z;
    }

    private List<Reward> rotateRewards(List<Reward> rewards) {
        Collections.rotate(rewards, 1);
        return rewards;
    }
}

