// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.reward.commands;

import com.google.common.collect.Lists;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import com.jonahseguin.drink.annotation.Text;
import dev.panda.crates.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RewardCommand {
    @Command(
            name = "",
            desc = "Main command of rewards"
    )
    public void root(@Sender CommandSender player) {
        player.sendMessage(CC.translate("&a&lReward Help"));
        player.sendMessage(CC.translate("&7- &e/reward setchance <chance> &7- &aSets the chance for the item in your hand"));
        player.sendMessage(CC.translate("&7- &e/reward setobligatory &7- &aSets the item in your hand to be obligatory"));
        player.sendMessage(CC.translate("&7- &e/reward addcommand <command> &7- &aAdds a command to the item in your hand"));
    }

    @Command(
            name = "setchance",
            desc = "Set's the chance of a reward"
    )
    public void setchance(@Sender Player player, int chance) {
        if (chance >= 0 && chance <= 100) {
            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null) {
                player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            } else {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta == null) {
                    player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
                } else {
                    List<String> lore = meta.getLore();
                    if (lore == null) {
                        lore = Lists.newArrayList();
                    }

                    lore.add(0, CC.translate("&7Chance:&a " + chance));
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                    player.sendMessage(CC.translate("&aSuccessfully set chance for &e" + itemStack.getType().name() + " &ato &e" + chance + "%"));
                }
            }
        } else {
            player.sendMessage(CC.translate("&cChance must be between 0 and 1.0!"));
        }
    }

    @Command(
            name = "setobligatory",
            desc = "Set's a reward obligatory."
    )
    public void setobligatory(@Sender Player player) {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            } else {
                List<String> lore = meta.getLore();
                if (lore == null) {
                    lore = Lists.newArrayList();
                }

                lore.add(0, CC.translate("&7Obligatory"));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                player.sendMessage(CC.translate("&aSuccessfully set obligatory for &e" + itemStack.getType().name()));
            }
        }
    }

    @Command(
            name = "addcommand",
            desc = "Add's a command to a reward."
    )
    public void addcommand(@Sender Player player, @Text String command) {
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                player.sendMessage(CC.translate("&cYou must be holding an item to set the chance!"));
            } else {
                List<String> lore = meta.getLore();
                if (lore == null) {
                    lore = Lists.newArrayList();
                }

                lore.add(0, CC.translate("&7Command:&a " + command));
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                player.sendMessage(CC.translate("&aSuccessfully added command for &e" + itemStack.getType().name()));
            }
        }
    }
}