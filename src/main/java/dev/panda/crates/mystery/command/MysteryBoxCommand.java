/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.mystery.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.utils.CC;
import org.bukkit.entity.Player;

@Require(value="pandacrates.mysterybox")
public class MysteryBoxCommand {
    @Command(name="create", desc="Create's a mystery box")
    public void mysterybox(@Sender Player player, String name) {
        if (Crate.getByName(name) != null) {
            player.sendMessage(CC.translate("&cCrate with that name already exists!"));
            return;
        }

        MysteryBox crate = new MysteryBox(name);
        crate.save();
        player.sendMessage(CC.translate("&aMystery &e" + name + "&a created!"));
    }
}

