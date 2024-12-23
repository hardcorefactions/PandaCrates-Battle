/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package dev.panda.crates.utils.item;

import dev.panda.crates.utils.chat.ChatUtil;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
    }

    public ItemBuilder(String material) {
        this.itemStack = new ItemStack(Material.valueOf(material), 1);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder(Material material, int damage) {
        this.itemStack = new ItemStack(material, 1, (short)damage);
    }

    public ItemBuilder(Material material, int amount, int damage) {
        this.itemStack = new ItemStack(material, amount, (short)damage);
    }

    public ItemBuilder name(String name) {
        if (name != null) {
            name = ChatUtil.translate(name);
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setDisplayName(name);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setLore(ChatUtil.translate(lore));
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public List<String> getLore() {
        return this.itemStack.getItemMeta().getLore();
    }

    public ItemBuilder lore(String ... lore) {
        if (lore != null) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.setLore(ChatUtil.translate(Arrays.asList(lore)));
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(boolean enchanted) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder enchant(boolean enchanted, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, level, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder enchant(boolean enchanted, Enchantment enchant, int level) {
        if (enchanted) {
            ItemMeta meta = this.itemStack.getItemMeta();
            meta.addEnchant(enchant, level, true);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder data(int dur) {
        this.itemStack.setDurability((short)dur);
        return this;
    }

    public ItemBuilder owner(String owner) {
        if (this.itemStack.getType() == CompatibleMaterial.HUMAN_SKULL.getMaterial()) {
            SkullMeta meta = (SkullMeta)this.itemStack.getItemMeta();
            if (meta == null) {
                return this;
            }
            meta.setOwner(owner);
            this.itemStack.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder armorColor(Color color) {
        try {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)this.itemStack.getItemMeta();
            leatherArmorMeta.setColor(color);
            this.itemStack.setItemMeta(leatherArmorMeta);
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("Error set armor color.");
        }
        return this;
    }

    public static String getDisplayName(ItemStack itemStack) {
        return itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : ChatUtil.toReadable(itemStack.getType());
    }

    public ItemStack build() {
        return this.itemStack;
    }
}

