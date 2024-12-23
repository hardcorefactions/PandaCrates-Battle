/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.reward;

import com.google.common.collect.Lists;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.ItemUtils;
import dev.panda.crates.utils.StringUtils;
import dev.panda.crates.utils.gson.Serializer;
import dev.panda.crates.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Setter
public class Reward {
    private ItemStack item;
    @Getter
    private double chance;
    @Getter
    private boolean initializedLore;
    @Getter
    private int slot;
    @Getter
    private RewardType type;
    @Getter
    private List<String> commands;
    @Getter
    private boolean obligatory;
    @Getter
    private LinkedList<String> broadcast = Lists.newLinkedList();

    public Reward(ItemStack item, double chance, int slot, RewardType type, List<String> commands, boolean obligatory) {
        this.item = item;
        if (this.item == null) {
            this.item = new ItemStack(Material.AIR);
        }
        this.chance = chance;
        this.slot = slot;
        this.type = type;
        this.commands = commands;
        this.obligatory = obligatory;
        if (this.chance > 1.0) {
            this.chance = 1.0;
        }
        ItemUtils.removeRewardsLore(this.item);
    }

    public Reward(Document document) {
        this.item = Serializer.GSON.fromJson(document.getString("item"), ItemStack.class);
        this.slot = document.getInteger("slot");
        if (document.get("chance") instanceof Double) {
            this.chance = document.getDouble("chance");
        } else if (document.get("chance") instanceof Integer) {
            this.chance = Double.parseDouble(document.getInteger("chance").toString());
        }
        if (this.chance > 1.0) {
            this.chance = 1.0;
        }
        this.type = document.containsKey("type") ? RewardType.getByName(document.getString("type")) : RewardType.ITEMS;
        if (document.containsKey("commands")) {
            this.commands = document.getList("commands", String.class);
        }
        if (document.containsKey("broadcast")) {
            this.setBroadcast(new LinkedList<>(document.getList("broadcast", String.class)));
        }
    }

    public ItemStack getItem() {
        if (this.item == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack item = this.item.clone();
        ItemUtils.removeRewardsLore(item);
        return item;
    }

    public ItemStack getPreviewItem() {
        ArrayList<String> lore = Lists.newArrayList();
        if (this.item == null) {
            lore.add("&CBUGGED ITEM");
            lore.add("&cREMOVE AND CREATE AGAIN.");
        } else {
            if (this.item.hasItemMeta() && this.item.getItemMeta().hasLore()) {
                lore.addAll(this.item.getItemMeta().getLore());
            }
            lore.add("&8Reward");
            lore.add("");
            lore.addAll(Arrays.asList(CC.CR_STRAIGHT_BAR + "Information: ", " " + CC.C_SQUARE + "&7Type: &b" + StringUtils.capitalize(this.type.name().toLowerCase()), " " + CC.C_SQUARE + "&7Chance: &a" + this.chance + "%"));
            if (this.commands != null && !this.commands.isEmpty()) {
                lore.add("");
                lore.add(CC.CR_STRAIGHT_BAR + "Commands:");
                for (String command : this.commands) {
                    lore.add(" " + CC.C_SQUARE + "&7/" + command);
                }
            }
            lore.add("");
            lore.add(CC.C_LEFT_ARROW + "Right Click &7to edit.");
        }
        return new ItemBuilder(this.item == null ? new ItemStack(Material.REDSTONE) : this.item.clone()).lore(lore).build();
    }

    public ItemStack getPreviewObligatoryItem() {
        ArrayList<String> lore = Lists.newArrayList();
        lore.add("&0");
        lore.add("&7Type: &e" + this.type.name());
        lore.add("&0");
        lore.add("&7Chance: &e" + this.chance + "%");
        lore.add("&cObligatory");
        if (this.commands != null && !this.commands.isEmpty()) {
            lore.add("&7Commands: &e");
            for (String command : this.commands) {
                lore.add(" &7- &f" + command);
            }
        }
        lore.add("&0");
        lore.add("&fRight Click &7to edit");
        return new ItemBuilder(this.item.clone()).lore(CC.translate(lore)).build();
    }

    public ItemStack getRealItem() {
        return this.item;
    }

    public Document serialize() {
        Document document = new Document();
        document.append("item", Serializer.GSON.toJson(this.item));
        document.append("slot", this.slot);
        document.append("chance", this.chance);
        document.append("type", this.type.name());
        if (this.commands != null) {
            document.append("commands", this.commands);
        }
        document.append("broadcast", new ArrayList<>(this.broadcast));
        return document;
    }

    public String getItemName() {
        return this.item.hasItemMeta() && this.item.getItemMeta().hasDisplayName() ? this.item.getItemMeta().getDisplayName() : StringUtils.capitalizeFully(this.item.getType().name());
    }

}

