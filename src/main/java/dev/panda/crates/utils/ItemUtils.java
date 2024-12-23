// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.utils;

import com.google.common.collect.Lists;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.reward.Reward;
import dev.panda.crates.reward.RewardType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ItemUtils {
    public static boolean isReward(ItemStack item) {
        if (item == null) {
            return false;
        } else if (!item.hasItemMeta()) {
            return false;
        } else {
            return item.getItemMeta().hasLore() && item.getItemMeta().getLore().stream().anyMatch((lore) -> lore.contains(CC.translate("&7Type: &e")));
        }
    }

    public static void consume(Player player) {
        ItemStack item = player.getInventory().getItemInHand();
        if (item.getAmount() == 1) {
            player.getInventory().remove(item);
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        player.updateInventory();
    }

    public static RewardType getRewardType(ItemStack item) {
        if (isReward(item)) {
            for (String lore : item.getItemMeta().getLore()) {
                if (lore.contains(CC.translate("&7Type: &e"))) {
                    return RewardType.getByName(ChatColor.stripColor(lore).replace(CC.translate("Type: "), "").toUpperCase());
                }
            }

        }
        return null;
    }

    public static Reward getRewardByItem(ItemStack item, int slot) {
        if (!isReward(item)) {
            return null;
        } else {
            RewardType type = getRewardType(item);
            if (type == null) {
                return null;
            } else {
                double chance = getChance(item);
                List<String> commands = Lists.newArrayList();
                if (type == RewardType.COMMAND) {
                    commands.addAll(Objects.requireNonNull(getCommands(item)));
                }

                return new Reward(item, chance, slot, type, commands, isObligatory(item));
            }
        }
    }

    public static void removeRewardsLore(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore != null) {
                lore.removeIf((line) -> line.contains(CC.translate("&7Type: &e")));
                lore.removeIf((line) -> line.contains(CC.translate("&7Chance: &e")));
                lore.removeIf((line) -> line.contains(CC.translate("&cObligatory")));
                lore.removeIf((line) -> line.contains(CC.translate("&7Commands: &e")));
                lore.removeIf((line) -> line.contains(CC.translate("&fRight Click &7to edit")));
                lore.removeIf((line) -> line.equalsIgnoreCase(CC.translate("&0")));
                if (lore.contains(CC.translate("&8Placeholder")) || lore.contains(CC.translate("&8Reward"))) {
                    lore.clear();
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }

    public static double getChance(ItemStack item) {
        if (isReward(item)) {
            for (String lore : item.getItemMeta().getLore()) {
                if (lore.contains(CC.translate("&7Chance: &e"))) {
                    return Double.parseDouble(ChatColor.stripColor(lore).replace(CC.translate("Chance: "), "").replace("%", ""));
                }
            }

        }
        return 0.0D;
    }

    public static boolean isObligatory(ItemStack item) {
        if (item == null) {
            return false;
        } else if (!item.hasItemMeta()) {
            return false;
        } else {
            return item.getItemMeta().hasLore() && item.getItemMeta().getLore().stream().anyMatch((lore) -> lore.contains(CC.translate("&cObligatory")));
        }
    }

    public static boolean isCommandType(ItemStack item) {
        if (item == null) {
            return false;
        } else if (!item.hasItemMeta()) {
            return false;
        } else {
            return item.getItemMeta().hasLore() && item.getItemMeta().getLore().stream().anyMatch((lore) -> lore.contains(CC.translate("&7Commands: &e")));
        }
    }

    public static List<String> getCommands(ItemStack item) {
        if (!isCommandType(item)) {
            return null;
        } else {
            List<String> commands = Lists.newArrayList();

            for(String lore : item.getItemMeta().getLore()) {
                if (lore.contains(CC.translate(" &7- &f"))) {
                    commands.add(ChatColor.stripColor(lore).replace(CC.translate(" - "), ""));
                }
            }

            return commands;
        }
    }

    public static boolean isAirDrop(ItemStack item) {
        if (item == null) {
            return false;
        } else if (!item.hasItemMeta()) {
            return false;
        } else if (!item.getItemMeta().hasLore()) {
            return false;
        } else {
            return item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equalsIgnoreCase(CC.translate("&6&lAirdrop"));
        }
    }

    private ItemUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
