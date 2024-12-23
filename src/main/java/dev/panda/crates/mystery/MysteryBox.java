/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.mystery;

import com.google.common.collect.Lists;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.RewardType;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ChatUtils;
import dev.panda.crates.utils.RandomUtils;
import dev.panda.crates.utils.item.ItemBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter
public class MysteryBox extends Crate {
    private List<Reward> obligatoryRewards = Lists.newArrayList();

    public MysteryBox(String name) {
        super(name);
    }

    @Override
    public List<String> getHologramsLines() {
        return Arrays.asList(this.getDisplayName() + " MisteryBox", "&7", "&fLeft click &7for preview rewards", "&fRight click &7to open", "&7", "&7&ostore.battle.rip");
    }

    @Override
    public ItemStack generateKey() {
        return new ItemBuilder(this.getKey().clone()).name(CC.translate(this.getDisplayName() + " Key")).lore(Lists.newArrayList("&7", "&7You can preview this " + this.getDisplayName() + " Crate &7 rewards", "&7By going to the overworld &aSpawn", "&7", ChatUtils.getFirstColor(this.getDisplayName()) + "Right Click &7to open the key", ChatUtils.getFirstColor(this.getDisplayName()) + "Left Click &7to preview rewards", "&7", "&7Purchase additional keys at &f&nstore.battle.rip")).build();
    }

    @Override
    public void openCrate(Player player) {
        List<String> commands;
        if (!this.isEnable()) {
            player.sendMessage(CC.translate("&cThis misterybox is currently disabled"));
            return;
        }

        if (this.getMaximumReward() == 0 || this.getRewards().isEmpty()) {
            player.sendMessage(CC.translate("&cCrate " + this.getName() + " is empty, please contact an admin."));
            return;
        }
        if (player.getInventory().firstEmpty() < 0) {
            player.sendMessage(CC.translate("&cInventory Full."));
            return;
        }

        int random = new Random().nextInt(this.getMaximumReward() - this.getMinimumReward() + 1) + this.getMinimumReward();
        ArrayList<Reward> randomRewards = new ArrayList<>();
        ArrayList<Reward> rewardList = new ArrayList<>(this.getRewards());
        Collections.shuffle(rewardList);
        for (int i = 0; i < random; ++i) {
            randomRewards.add(RandomUtils.getRandomReward(rewardList));
        }
        for (Reward reward : this.obligatoryRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                commands = reward.getCommands();
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
            if (reward.getBroadcast().isEmpty()) continue;
            reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
        }
        for (Reward reward : randomRewards) {
            if (reward.getType() == RewardType.ITEMS) {
                if (player.getInventory().firstEmpty() < 0) {
                    player.sendMessage(CC.translate("&cInventory Full."));
                    player.getWorld().dropItem(player.getLocation(), reward.getItem());
                } else {
                    player.getInventory().addItem(reward.getItem());
                }
            } else {
                commands = reward.getCommands();
                for (String command : commands) {
                    if (command.contains("op")) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                }
            }
            if (reward.getBroadcast().isEmpty()) continue;
            reward.getBroadcast().forEach(line -> Bukkit.broadcastMessage(line.replace("{player}", player.getName())));
        }
        if (this.getOpenSound() != null) {
            player.playSound(player.getLocation(), this.getOpenSound(), 1.0f, 1.0f);
        }
        player.sendMessage(CC.translate("&aYou have received " + (randomRewards.size() + this.obligatoryRewards.size()) + " reward(s)"));
        this.consumeKey(player);
        player.updateInventory();
    }

    public Reward getObligatoryReward(int slot) {
        return this.obligatoryRewards.stream().filter(reward -> reward.getSlot() == slot).findFirst().orElse(null);
    }

}

