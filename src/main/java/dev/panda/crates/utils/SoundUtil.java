/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils;

import dev.panda.crates.utils.chat.ChatUtil;
import dev.panda.crates.utils.compatibility.sound.CompatibleSound;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundUtil {
    public static void play(Player player, Sound sound) {
        try {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (Exception ex) {
            ChatUtil.log("&cSound '" + sound + "' is not valid, please use a valid sound.");
        }
    }

    public static void play(Player player, String name) {
        CompatibleSound sound = CompatibleSound.getSound(name);
        if (sound == null) {
            return;
        }
        SoundUtil.play(player, sound.getSound());
    }

    private SoundUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

