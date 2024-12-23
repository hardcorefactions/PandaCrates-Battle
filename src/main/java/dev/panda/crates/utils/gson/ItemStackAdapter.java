/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.EnchantmentStorageMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.MapMeta
 *  org.bukkit.inventory.meta.PotionMeta
 *  org.bukkit.inventory.meta.Repairable
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.potion.PotionEffect
 */
package dev.panda.crates.utils.gson;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Type;
import java.util.*;

public class ItemStackAdapter
implements JsonDeserializer<ItemStack>,
JsonSerializer<ItemStack> {
    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String DAMAGE = "damage";
    public static final String NAME = "name";
    public static final String LORE = "lore";
    public static final String ENCHANTS = "enchants";
    public static final String REPAIRCOST = "repaircost";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_PAGES = "pages";
    public static final String LEATHER_ARMOR_COLOR = "color";
    public static final String MAP_SCALING = "scaling";
    public static final String SKULL_OWNER = "skull";
    public static final String POTION_EFFECTS_OLD = "effects";
    public static final String POTION_EFFECTS = "potion-effects";
    public static final String STORED_ENCHANTS = "stored-enchants";
    public static final int DEFAULT_ID;
    public static final int DEFAULT_COUNT;
    public static final int DEFAULT_DAMAGE;

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return ItemStackAdapter.serialize(src);
    }

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ItemStackAdapter.deserialize(json);
    }

    public static JsonObject serialize(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (stack.getType().getId() == 0) {
            return null;
        }
        if (stack.getAmount() == 0) {
            return null;
        }
        JsonObject json = new JsonObject();
        ItemStackAdapter.transferAll(stack, json, true);
        return json;
    }

    public static ItemStack deserialize(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }
        if (!jsonElement.isJsonObject()) {
            return null;
        }
        JsonObject json = jsonElement.getAsJsonObject();
        ItemStack stack = ItemStackAdapter.createItemStack();
        ItemStackAdapter.transferAll(stack, json, false);
        return stack;
    }

    public static ItemStack createItemStack() {
        return new ItemStack(Material.AIR);
    }

    public static void transferAll(ItemStack stack, JsonObject json, boolean stack2json) {
        ItemStackAdapter.transferBasic(stack, json, stack2json);
        ItemMeta meta = stack.getItemMeta();
        ItemStackAdapter.transferMeta(meta, json, stack2json);
        if (!stack2json) {
            stack.setItemMeta(meta);
        }
    }

    public static void transferBasic(ItemStack stack, JsonObject json, boolean stack2json) {
        ItemStackAdapter.transferId(stack, json, stack2json);
        ItemStackAdapter.transferCount(stack, json, stack2json);
        ItemStackAdapter.transferDamage(stack, json, stack2json);
    }

    public static void transferId(ItemStack stack, JsonObject json, boolean stack2json) {
        if (stack2json) {
            String id = stack.getType().name();
            if (id.equals("AIR")) {
                return;
            }
            json.addProperty(ID, id);
        } else {
            JsonElement element = json.get(ID);
            if (element == null) {
                return;
            }
            stack.setType(Material.valueOf(element.getAsString()));
        }
    }

    public static void transferCount(ItemStack stack, JsonObject json, boolean stack2json) {
        if (stack2json) {
            int count = stack.getAmount();
            if (count == DEFAULT_COUNT) {
                return;
            }
            json.addProperty(COUNT, count);
        } else {
            JsonElement element = json.get(COUNT);
            if (element == null) {
                return;
            }
            stack.setAmount(element.getAsInt());
        }
    }

    public static void transferDamage(ItemStack stack, JsonObject json, boolean stack2json) {
        if (stack2json) {
            short damage = stack.getDurability();
            if (damage == DEFAULT_DAMAGE) {
                return;
            }
            json.addProperty(DAMAGE, damage);
        } else {
            JsonElement element = json.get(DAMAGE);
            if (element == null) {
                return;
            }
            stack.setDurability(element.getAsShort());
        }
    }

    public static void transferMeta(ItemMeta meta, JsonObject json, boolean meta2json) {
        ItemStackAdapter.transferUnspecificMeta(meta, json, meta2json);
        ItemStackAdapter.transferSpecificMeta(meta, json, meta2json);
    }

    public static void transferUnspecificMeta(ItemMeta meta, JsonObject json, boolean meta2json) {
        ItemStackAdapter.transferName(meta, json, meta2json);
        ItemStackAdapter.transferLore(meta, json, meta2json);
        ItemStackAdapter.transferEnchants(meta, json, meta2json);
        ItemStackAdapter.transferRepaircost(meta, json, meta2json);
    }

    public static void transferName(ItemMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasDisplayName()) {
                return;
            }
            json.addProperty(NAME, meta.getDisplayName());
        } else {
            JsonElement element = json.get(NAME);
            if (element == null) {
                return;
            }
            meta.setDisplayName(element.getAsString());
        }
    }

    public static void transferLore(ItemMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasLore()) {
                return;
            }
            json.add(LORE, ItemStackAdapter.convertStringList(meta.getLore()));
        } else {
            JsonElement element = json.get(LORE);
            if (element == null) {
                return;
            }
            meta.setLore(ItemStackAdapter.convertStringList(element));
        }
    }

    public static void transferEnchants(ItemMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasEnchants()) {
                return;
            }
            json.add(ENCHANTS, ItemStackAdapter.convertEnchantLevelMap(meta.getEnchants()));
        } else {
            JsonElement element = json.get(ENCHANTS);
            if (element == null) {
                return;
            }
            for (Map.Entry<Enchantment, Integer> entry : ItemStackAdapter.convertEnchantLevelMap(element).entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
    }

    public static void transferRepaircost(ItemMeta meta, JsonObject json, boolean meta2json) {
        if (!(meta instanceof Repairable)) {
            return;
        }
        Repairable repairable = (Repairable)meta;
        if (meta2json) {
            if (!repairable.hasRepairCost()) {
                return;
            }
            json.addProperty(REPAIRCOST, repairable.getRepairCost());
        } else {
            JsonElement element = json.get(REPAIRCOST);
            if (element == null) {
                return;
            }
            repairable.setRepairCost(element.getAsInt());
        }
    }

    public static void transferSpecificMeta(ItemMeta meta, JsonObject json, boolean meta2json) {
        if (meta instanceof BookMeta) {
            ItemStackAdapter.transferBookMeta((BookMeta)meta, json, meta2json);
        } else if (meta instanceof LeatherArmorMeta) {
            ItemStackAdapter.transferLeatherArmorMeta((LeatherArmorMeta)meta, json, meta2json);
        } else if (meta instanceof MapMeta) {
            ItemStackAdapter.transferMapMeta((MapMeta)meta, json, meta2json);
        } else if (meta instanceof PotionMeta) {
            ItemStackAdapter.transferPotionMeta((PotionMeta)meta, json, meta2json);
        } else if (meta instanceof SkullMeta) {
            ItemStackAdapter.transferSkullMeta((SkullMeta)meta, json, meta2json);
        } else if (meta instanceof EnchantmentStorageMeta) {
            ItemStackAdapter.transferEnchantmentStorageMeta((EnchantmentStorageMeta)meta, json, meta2json);
        }
    }

    public static void transferBookMeta(BookMeta meta, JsonObject json, boolean meta2json) {
        ItemStackAdapter.transferTitle(meta, json, meta2json);
        ItemStackAdapter.transferAuthor(meta, json, meta2json);
        ItemStackAdapter.transferPages(meta, json, meta2json);
    }

    public static void transferTitle(BookMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle()) {
                return;
            }
            json.addProperty(BOOK_TITLE, meta.getTitle());
        } else {
            JsonElement element = json.get(BOOK_TITLE);
            if (element == null) {
                return;
            }
            meta.setTitle(element.getAsString());
        }
    }

    public static void transferAuthor(BookMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle()) {
                return;
            }
            json.addProperty(BOOK_AUTHOR, meta.getAuthor());
        } else {
            JsonElement element = json.get(BOOK_AUTHOR);
            if (element == null) {
                return;
            }
            meta.setAuthor(element.getAsString());
        }
    }

    public static void transferPages(BookMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasTitle()) {
                return;
            }
            json.add(BOOK_PAGES, ItemStackAdapter.convertStringList(meta.getPages()));
        } else {
            JsonElement element = json.get(BOOK_PAGES);
            if (element == null) {
                return;
            }
            meta.setPages(ItemStackAdapter.convertStringList(element));
        }
    }

    public static void transferLeatherArmorMeta(LeatherArmorMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            Color color = meta.getColor();
            if (Bukkit.getItemFactory().getDefaultLeatherColor().equals(color)) {
                return;
            }
            json.addProperty(LEATHER_ARMOR_COLOR, color.asRGB());
        } else {
            JsonElement element = json.get(LEATHER_ARMOR_COLOR);
            if (element == null) {
                return;
            }
            meta.setColor(Color.fromRGB(element.getAsInt()));
        }
    }

    public static void transferMapMeta(MapMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.isScaling()) {
                return;
            }
            json.addProperty(MAP_SCALING, true);
        } else {
            JsonElement element = json.get(MAP_SCALING);
            if (element == null) {
                return;
            }
            meta.setScaling(element.getAsBoolean());
        }
    }

    public static void transferPotionMeta(PotionMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasCustomEffects()) {
                return;
            }
            json.add(POTION_EFFECTS, ItemStackAdapter.convertPotionEffectList(meta.getCustomEffects()));
        } else {
            JsonElement element = json.get(POTION_EFFECTS);
            if (element == null) {
                element = json.get(POTION_EFFECTS_OLD);
            }
            if (element == null) {
                return;
            }
            meta.clearCustomEffects();
            for (PotionEffect pe : ItemStackAdapter.convertPotionEffectList(element)) {
                meta.addCustomEffect(pe, false);
            }
        }
    }

    public static void transferSkullMeta(SkullMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasOwner()) {
                return;
            }
            json.addProperty(SKULL_OWNER, meta.getOwner());
        } else {
            JsonElement element = json.get(SKULL_OWNER);
            if (element == null) {
                return;
            }
            meta.setOwner(element.getAsString());
        }
    }

    public static void transferEnchantmentStorageMeta(EnchantmentStorageMeta meta, JsonObject json, boolean meta2json) {
        if (meta2json) {
            if (!meta.hasStoredEnchants()) {
                return;
            }
            json.add(STORED_ENCHANTS, ItemStackAdapter.convertEnchantLevelMap(meta.getStoredEnchants()));
        } else {
            JsonElement element = json.get(STORED_ENCHANTS);
            if (element == null) {
                return;
            }
            for (Map.Entry<Enchantment, Integer> entry : ItemStackAdapter.convertEnchantLevelMap(element).entrySet()) {
                meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
            }
        }
    }

    public static JsonArray convertStringList(Collection<String> strings) {
        JsonArray ret = new JsonArray();
        for (String string : strings) {
            ret.add(new JsonPrimitive(string));
        }
        return ret;
    }

    public static List<String> convertStringList(JsonElement jsonElement) {
        JsonArray array = jsonElement.getAsJsonArray();
        ArrayList<String> ret = new ArrayList<>();
        for (JsonElement element : array) {
            ret.add(element.getAsString());
        }
        return ret;
    }

    public static JsonArray convertPotionEffectList(Collection<PotionEffect> potionEffects) {
        JsonArray ret = new JsonArray();
        for (PotionEffect e : potionEffects) {
            ret.add(PotionEffectAdapter.toJson(e));
        }
        return ret;
    }

    public static List<PotionEffect> convertPotionEffectList(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }
        if (!jsonElement.isJsonArray()) {
            return null;
        }
        JsonArray array = jsonElement.getAsJsonArray();
        ArrayList<PotionEffect> ret = new ArrayList<>();
        for (JsonElement element : array) {
            PotionEffect e = PotionEffectAdapter.fromJson(element);
            if (e == null) continue;
            ret.add(e);
        }
        return ret;
    }

    public static JsonObject convertEnchantLevelMap(Map<Enchantment, Integer> enchantLevelMap) {
        JsonObject ret = new JsonObject();
        for (Map.Entry<Enchantment, Integer> entry : enchantLevelMap.entrySet()) {
            ret.addProperty(entry.getKey().getName(), entry.getValue());
        }
        return ret;
    }

    public static Map<Enchantment, Integer> convertEnchantLevelMap(JsonElement jsonElement) {
        JsonObject json = jsonElement.getAsJsonObject();
        HashMap<Enchantment, Integer> ret = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String id = entry.getKey();
            Enchantment ench = Enchantment.getByName(id);
            int lvl = entry.getValue().getAsInt();
            ret.put(ench, lvl);
        }
        return ret;
    }

    static {
        ItemStack stack = ItemStackAdapter.createItemStack();
        DEFAULT_ID = stack.getType().getId();
        DEFAULT_COUNT = stack.getAmount();
        DEFAULT_DAMAGE = stack.getDurability();
    }
}

