/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.crate.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Require(value="pandacrates.keyall")
public class KeyAllDoneCommand {
    @Command(name="", desc="Send's a title that says key all finished.")
    public void keyalldone(@Sender CommandSender sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
        }
    }
}

