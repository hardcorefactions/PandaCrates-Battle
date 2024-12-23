/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.airdrop.command;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.airdrop.Airdrop;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.menus.CratePreviewMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.entity.Player;

@Require(value="pandacrates.admin")
public class AirDropCommand {
    @Command(name="", desc="Main command for air drop")
    public void root(@Sender Player player) {
        Airdrop airdrop = (Airdrop) Crate.getByName("Airdrop");
        if (airdrop == null) {
            player.sendMessage(CC.translate("&cAirdrop with that name does not exist!"));
            return;
        }

        new CratePreviewMenu(airdrop).openMenu(player);
    }

    @Command(name="edit", desc="Allows you to edit the air drop crate.")
    public void airdropEdit(@Sender Player player) {
        Crate crate = Crate.getByName("Airdrop");
        if (crate == null) {
            player.sendMessage(CC.translate("&cCrate with that name does not exist!"));
            return;
        }
        new CrateEditMenu(crate).openMenu(player);
    }
}

