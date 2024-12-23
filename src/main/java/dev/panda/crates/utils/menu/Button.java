/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package dev.panda.crates.utils.menu;

import com.google.common.base.Joiner;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Button {
    public static Button placeholder(Material material, byte data, String ... title) {
        return Button.placeholder(material, data, title == null || title.length == 0 ? " " : Joiner.on(" ").join(title));
    }

    public static Button placeholder(Material material) {
        return Button.placeholder(material, " ");
    }

    public static Button placeholder(Material material, String title) {
        return Button.placeholder(material, (byte)0, title);
    }

    public static Button placeholder(final Material material, final byte data, final String title) {
        return new Button(){

            @Override
            public String getName(Player player) {
                return title;
            }

            @Override
            public List<String> getDescription(Player player) {
                return Collections.emptyList();
            }

            @Override
            public Material getMaterial(Player player) {
                return material;
            }

            @Override
            public byte getDamageValue(Player player) {
                return data;
            }
        };
    }

    public static Button fromItem(final ItemStack item) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return item;
            }
        };
    }

    public static Button fromItem(final ItemStack item, final Consumer<Player> consumer) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return item;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (consumer != null) {
                    consumer.accept(player);
                }
            }
        };
    }

    public static Button fromItem(final ItemStack item, final BiConsumer<Player, ClickType> biConsumer) {
        return new Button(){

            @Override
            public ItemStack getButtonItem(Player player) {
                return item;
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType) {
                if (biConsumer != null) {
                    biConsumer.accept(player, clickType);
                }
            }
        };
    }

    public String getName(Player player) {
        return "";
    }

    public List<String> getDescription(Player player) {
        return null;
    }

    public Material getMaterial(Player player) {
        return null;
    }

    public byte getDamageValue(Player player) {
        return 0;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
    }

    public void clicked(InventoryClickEvent event) {
    }

    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    public int getAmount(Player player) {
        return 1;
    }

    public ItemStack getButtonItem(Player player) {
        List<String> description;
        if (this.getMaterial(player) == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack buttonItem = new ItemStack(this.getMaterial(player), this.getAmount(player), this.getDamageValue(player));
        ItemMeta meta = buttonItem.getItemMeta();
        if (this.getName(player) != null) {
            meta.setDisplayName(CC.translate(this.getName(player)));
        }
        if ((description = this.getDescription(player)) != null) {
            meta.setLore(CC.translate(description));
        }
        buttonItem.setItemMeta(meta);
        return buttonItem;
    }

    public static void playFail(Player player) {
        player.playSound(player.getLocation(), CompatibleSound.DIG_GRASS.getSound(), 20.0f, 0.1f);
    }

    public static void playSuccess(Player player) {
        player.playSound(player.getLocation(), CompatibleSound.NOTE_PIANO.getSound(), 20.0f, 15.0f);
    }

    public static void playNeutral(Player player) {
        player.playSound(player.getLocation(), CompatibleSound.CLICK.getSound(), 20.0f, 1.0f);
    }
}

