/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package dev.panda.crates.crate.commands;

import com.google.common.base.Joiner;
import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.Crates;
import dev.panda.crates.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class PandaCommand {
    private static final Joiner JOINER = Joiner.on(", ");

    @Command(name="", desc="Current Status of panda plugin")
    public void root(@Sender Player player) {
        PluginDescriptionFile desc = Crates.getInstance().getDescription();
        player.sendMessage(" ");
        player.sendMessage(CC.BLUE + CC.BOLD + "Panda Crates");
        player.sendMessage(" ");
        player.sendMessage(CC.GRAY + "  Version: " + CC.AQUA + desc.getVersion());
        player.sendMessage(CC.GRAY + "  Authors: " + CC.AQUA + JOINER.join(desc.getAuthors()));
        player.sendMessage(CC.GRAY + "  MC-Market: https://builtbybit.com/resources/pandacrates.25308/");
        player.sendMessage(" ");
    }
}

