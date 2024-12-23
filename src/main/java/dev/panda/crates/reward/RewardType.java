/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.reward;

public enum RewardType {
    COMMAND,
    ITEMS;


    public static RewardType getByName(String name) {
        for (RewardType type : RewardType.values()) {
            if (!type.name().equalsIgnoreCase(name)) continue;
            return type;
        }
        return null;
    }
}

