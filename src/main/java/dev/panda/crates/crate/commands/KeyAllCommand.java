/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.managers.hologram.title.Title;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Require(value="pandacrates.keyall")
public class KeyAllCommand {
    @Command(name="", desc="Performs a key all.")
    public void keyAll(@Sender CommandSender sender, Crate crate, int amount) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            ItemStack item = crate.generateKey().clone();
            item.setAmount(amount);
            if (target.getInventory().firstEmpty() != -1) {
                target.getInventory().addItem(item);
            } else {
                target.getWorld().dropItem(target.getLocation(), item);
            }
            TitleUtil.sendTitle(Crates.getInstance().getLoader(), target, Title.builder().title(crate.getDisplayName() + " " + CC.GREEN + "received.").subtitle(CC.YELLOW + "Amount: " + CC.WHITE + amount).build());
        }
        sender.sendMessage(CC.translate("&eYou have given &6x" + amount + " " + crate.getName() + " &eto &6everybody&e."));
    }
}

