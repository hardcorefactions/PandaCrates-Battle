/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package dev.panda.crates.utils.compatibility.material;

import dev.panda.crates.utils.ServerUtil;
import org.bukkit.Material;

public enum CompatibleMaterial {
    HUMAN_SKULL("SKULL_ITEM", "LEGACY_SKULL_ITEM"),
    WOOL("WOOL", "LEGACY_WOOL"),
    SNOW_BALL("SNOW_BALL", "LEGACY_SNOW_BALL"),
    CARPET("CARPET", "LEGACY_CARPET"),
    STAINED_GLASS_PANE("STAINED_GLASS_PANE", "LEGACY_STAINED_GLASS_PANE"),
    INK_SACK("INK_SACK", "LEGACY_INK_SACK"),
    ANVIL("ANVIL", "LEGACY_ANVIL"),
    BOOK_AND_QUILL("BOOK_AND_QUILL", "LEGACY_BOOK_AND_QUILL"),
    EXP_BOTTLE("EXP_BOTTLE", "LEGACY_EXP_BOTTLE"),
    FIREWORK("FIREWORK", "LEGACY_FIREWORK"),
    WATCH("WATCH", "LEGACY_WATCH"),
    FIREBALL("FIREBALL", "LEGACY_FIREBALL"),
    EYE_OF_ENDER("EYE_OF_ENDER", "LEGACY_EYE_OF_ENDER"),
    EMPTY_MAP("EMPTY_MAP", "LEGACY_EMPTY_MAP"),
    WOOD_BUTTON("WOOD_BUTTON", "LEGACY_WOOD_BUTTON"),
    BED("BED", "LEGACY_BED"),
    ENDER_PORTAL("ENDER_PORTAL", "LEGACY_ENDER_PORTAL"),
    REDSTONE_TORCH_ON("REDSTONE_TORCH_ON", "LEGACY_REDSTONE_TORCH_ON"),
    REDSTONE_TORCH_OFF("REDSTONE_TORCH_OFF", "LEGACY_REDSTONE_TORCH_OFF");

    private final String material8;
    private final String material13;

    CompatibleMaterial(String material8, String material13) {
        this.material8 = material8;
        this.material13 = material13;
    }

    CompatibleMaterial(String material8) {
        this(material8, null);
    }

    public Material getMaterial() {
        if (ServerUtil.SERVER_VERSION_INT <= 12) {
            return this.material8 == null ? Material.valueOf("SKULL_ITEM") : Material.valueOf(this.material8);
        }
        return this.material13 == null ? Material.valueOf("LEGACY_SKULL_ITEM") : Material.valueOf(this.material13);
    }
}

