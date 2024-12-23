/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate;

import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.gson.Serializer;
import dev.panda.crates.utils.item.ItemBuilder;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class CratePlaceholder {
    private final ItemStack item;
    private final int slot;

    public CratePlaceholder(Document document) {
        this.item = Serializer.GSON.fromJson(document.getString("item"), ItemStack.class);
        this.slot = document.getInteger("slot");
    }

    public ItemStack getPreview() {
        return new ItemBuilder(this.item.clone()).name(" ").lore("").build();
    }

    public ItemStack getEditPreview() {
        ItemBuilder builder = new ItemBuilder(this.item.clone());
        List<String> lore = builder.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.addAll(Arrays.asList("&8Placeholder", "", CC.CR_STRAIGHT_BAR + "&cThis item has not been ", CC.CR_STRAIGHT_BAR + "&cconfigured yet.", "", CC.A_LEFT_ARROW + "Right Click &7to configure."));
        return new ItemBuilder(this.item.clone()).lore(lore).build();
    }

    public Document serialize() {
        return new Document("item", Serializer.GSON.toJson(this.item)).append("slot", this.slot);
    }

    public CratePlaceholder(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }
}

