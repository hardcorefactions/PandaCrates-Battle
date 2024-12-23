/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.crate.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import dev.panda.crates.Crates;
import dev.panda.crates.airdrop.Airdrop;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.CSGOOpenMenu;
import dev.panda.crates.crate.menus.CrateEditMenu;
import dev.panda.crates.crate.menus.CrateManageMenu;
import dev.panda.crates.crate.menus.CratePreviewMenu;
import dev.panda.crates.crate.menus.types.RulettaMenu;
import dev.panda.crates.mystery.MysteryBox;
import dev.panda.crates.utils.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@Require(value="pandacrates.admin")
public class CrateCommands {
    @Command(name="", desc="Main command for crate.")
    public void root(@Sender Player player) {
        player.sendMessage(" ");
        player.sendMessage(CC.translate("&c&l♥ &b&lCrate Command &c&l♥"));
        Arrays.asList("", " " + CC.C_LEFT_ARROW + "/crate create <name> " + CC.CR_STRAIGHT_BAR + "Creates a new crate.", " " + CC.C_LEFT_ARROW + "/crate delete <name> " + CC.CR_STRAIGHT_BAR + "Deletes an existing crate.", " " + CC.C_LEFT_ARROW + "/crate list " + CC.CR_STRAIGHT_BAR + "Lists all the existing crates.", " " + CC.C_LEFT_ARROW + "/crate edit <name> " + CC.CR_STRAIGHT_BAR + "Edits an existing crate.", " " + CC.C_LEFT_ARROW + "/crate preview <name> " + CC.CR_STRAIGHT_BAR + "Previews an existing crate.", " " + CC.C_LEFT_ARROW + "/crate give <name> <amount> <player> " + CC.CR_STRAIGHT_BAR + "Give a key to a player.", " " + CC.C_LEFT_ARROW + "/crate manage " + CC.CR_STRAIGHT_BAR + "Opens a menu to manage each crate.", " " + CC.C_LEFT_ARROW + "/crate setchest " + CC.CR_STRAIGHT_BAR + "Set's the crate block and hologram.", "").forEach(command -> player.sendMessage(CC.translate(command)));
    }

    @Command(name="manage", desc="Manage every crate.")
    public void manage(@Sender Player player) {
        new CrateManageMenu().open(player);
    }

    @Command(name="create", desc="Create's a new crate.")
    public void create(@Sender Player player, String name) {
        if (Crate.getByName(name) != null) {
            player.sendMessage(CC.translate("&cCrate with that name already exists!"));
            return;
        }
        Crate crate = new Crate(name);
        crate.save();
        player.sendMessage(CC.translate("&aCrate &e" + name + "&a created!"));
    }

    @Command(name="delete", desc="Allows you to delete a crate", aliases={"remove"})
    public void delete(@Sender Player player, Crate crate) {
        crate.delete();
        player.sendMessage(CC.translate("&aCrate &e" + crate.getName() + "&a deleted!"));
    }

    @Command(name="list", desc="Lists every crate")
    public void list(@Sender Player player) {
        player.sendMessage(CC.translate("&9&lCrates:"));
        player.sendMessage("");
        if (Crate.getCrates().isEmpty()) {
            player.sendMessage(CC.translate("&cThere are no crates."));
            return;
        }
        for (Crate crate : Crate.getCrates().values()) {
            if (crate instanceof MysteryBox) {
                player.sendMessage(CC.translate("&7- &e" + crate.getName() + " &7- &aMystery Box"));
                continue;
            }
            if (crate instanceof Airdrop) {
                player.sendMessage(CC.translate("&7- &e" + crate.getName() + " &7- &aAirdrop"));
                continue;
            }
            player.sendMessage(CC.translate("&7- &9" + crate.getName() + " &7- &7Crate"));
        }
    }

    @Command(name="edit", desc="Allows you to edit a crate.")
    public void edit(@Sender Player player, Crate crate) {
        new CrateEditMenu(crate).openMenu(player);
    }

    @Command(name="test", desc="Test a crate.")
    public void test(@Sender Player player, Crate crate) {
        new CSGOOpenMenu(crate).openMenu(player);
    }

    @Command(name="preview", desc="Opens a menu with the preview of the crate.")
    public void preview(@Sender Player player, Crate crate) {
        new CratePreviewMenu(crate).openMenu(player);
    }

    @Command(name="give", desc="Gives a crate to a player.")
    public void give(@Sender CommandSender player, Crate crate, int amount, Player target) {
        ItemStack item = crate.generateKey().clone();
        item.setAmount(amount);
        target.getInventory().addItem(item);
        if (crate instanceof MysteryBox) {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Mistery&e Keys to &6" + target.getName() + "&e!"));
        } else if (crate instanceof Airdrop) {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Airdrop&e to &6" + target.getName() + "&e!"));
        } else {
            player.sendMessage(CC.translate("&eYou have given x" + amount + " &6Crate Keys&e to &6" + target.getName() + "&e!"));
        }
    }

    @Command(name="setchest", desc="Set's the chest of a crate (you must look at it).")
    public void setchest(@Sender Player player, Crate crate) {
        Block targetBlock;
        try {
            Method method = LivingEntity.class.getDeclaredMethod("getTargetBlock", Set.class, Integer.TYPE);
            targetBlock = (Block)method.invoke(player, null, 4);
        } catch (NoSuchMethodException ex) {
            player.setMetadata("breakBlockToSetChest", new FixedMetadataValue(Crates.getInstance(), crate));
            player.sendMessage(CC.YELLOW + "Please break the block you would like the crate to be.");
            player.sendMessage(CC.YELLOW + "Please break the block you would like the crate to be.");
            return;
        } catch (IllegalAccessException | InvocationTargetException e) {
            player.sendMessage(CC.RED + "Error occured.");
            e.printStackTrace();
            return;
        }
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be looking at block."));
            return;
        }
        Location location = targetBlock.getLocation();
        crate.handleSetChest(player, location);
    }

    @Command(name="removechest", desc="Removes the chest of the crate.")
    public void removeLocation(@Sender CommandSender sender, Crate crate) {
        crate.setChestLocation(null);
        crate.removeHologram();
        sender.sendMessage(CC.translate("&aSuccessfully removed chest location."));
    }

    @Command(name="save", desc="Saves")
    public void savejson(@Sender Player player) {
        Crate.saveAll();
        player.sendMessage(CC.GREEN + "Saved every crate into crates.json.");
    }

    @Command(name="rulette", desc="Open rulette menu")
    @Require(value="crates.dev")
    public void rulette(@Sender Player player, Crate crate) {
        new RulettaMenu(crate).open(player);
    }
}

