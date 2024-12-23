/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.utils.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Require(value="pandacrates.givekey")
public class GiveKeyCommand {
    @Command(name="", desc="Give's a player a key.")
    public void givekey(@Sender CommandSender player, Player target, Crate crate, int amount) {
        ItemStack item = crate.generateKey().clone();
        item.setAmount(amount);
        if (target.getInventory().firstEmpty() == -1) {
            target.sendMessage("");
            target.sendMessage(CC.translate("&a&eYour inventory was &cfull&e."));
            return;
        }
        target.getInventory().addItem(item);
    }
}

