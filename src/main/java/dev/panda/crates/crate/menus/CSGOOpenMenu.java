// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.crate.menus;

import com.google.common.collect.Maps;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.utils.RandomUtils;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CSGOOpenMenu extends Menu {
    private final Crate crate;
    private final Reward randomReward;
    private final List<Reward> rewards;
    int time = 0;
    private boolean claimed = false;

    public String getTitle(Player player) {
        return this.crate.getDisplayName() + " Crate";
    }

    public CSGOOpenMenu(Crate crate) {
        this.crate = crate;
        this.rewards = crate.getRewards();
        this.randomReward = RandomUtils.getRandomReward(this.rewards);
        Collections.shuffle(this.rewards);
        this.setAutoUpdate(true);
        this.setUpdateAfterClick(false);
    }

    public void onClose(Player player) {
        if (!this.claimed) {
            player.getInventory().addItem(this.randomReward.getItem());
            player.updateInventory();
            player.playSound(player.getLocation(), CompatibleSound.LEVEL_UP.getSound(), 1.0F, 1.0F);
            this.claimed = true;
        }

    }

    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        Collections.rotate(this.rewards, 1);

        for(int i = 0; i < 9; ++i) {
            buttons.put(buttons.size(), Button.fromItem((new ItemBuilder(Material.STAINED_GLASS_PANE)).data(0).build()));
        }

        for(int i = 0; i < 9; ++i) {
            buttons.put(buttons.size(), Button.fromItem(this.rewards.get(i).getItem()));
        }

        for(int i = 18; i < 27; ++i) {
            buttons.put(buttons.size(), Button.fromItem((new ItemBuilder(Material.STAINED_GLASS_PANE)).data(0).build()));
        }

        buttons.put(4, Button.fromItem((new ItemBuilder(Material.STAINED_GLASS_PANE)).data(5).build()));
        buttons.put(22, Button.fromItem((new ItemBuilder(Material.STAINED_GLASS_PANE)).data(5).build()));
        player.playSound(player.getLocation(), CompatibleSound.NOTE_STICKS.getSound(), 1.0F, 1.0F);
        Button button = buttons.get(13);
        if (this.time == 25) {
            if (button.getButtonItem(player).isSimilar(this.randomReward.getItem())) {
                this.setAutoUpdate(false);
                player.getInventory().addItem(this.randomReward.getItem());
                player.updateInventory();
                player.playSound(player.getLocation(), CompatibleSound.LEVEL_UP.getSound(), 1.0F, 1.0F);
                this.claimed = true;
            }
        } else {
            ++this.time;
        }

        return buttons;
    }
}