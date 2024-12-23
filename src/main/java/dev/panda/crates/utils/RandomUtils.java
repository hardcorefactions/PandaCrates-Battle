/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.utils;

import dev.panda.crates.Crates;
import dev.panda.crates.reward.Reward;
import lombok.Getter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomUtils {
    @Getter
    private static final Random random = ThreadLocalRandom.current();

    public static double getDouble(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static Reward getRandomReward(List<Reward> list) {
        list.removeIf(reward -> reward.getChance() <= 0.0);
        if (list.isEmpty()) {
            return null;
        }
        Reward reward2 = null;
        double total = list.stream().mapToDouble(Reward::getChance).sum();
        double index = RandomUtils.getDouble(0.0, total);
        double countWeight = 0.0;
        for (Reward reward1 : list) {
            if (!((countWeight += reward1.getChance()) >= index)) continue;
            reward2 = reward1;
            break;
        }
        return reward2;
    }

    private RandomUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

}

