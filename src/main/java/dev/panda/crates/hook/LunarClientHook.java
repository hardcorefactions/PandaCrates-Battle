/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.lunarclient.bukkitapi.LunarClientAPI
 *  com.lunarclient.bukkitapi.title.LCTitleBuilder
 *  com.lunarclient.bukkitapi.title.TitleType
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.hook;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.title.LCTitleBuilder;
import com.lunarclient.bukkitapi.title.TitleType;
import dev.panda.crates.managers.hologram.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;

public class LunarClientHook {
    public void sendTitle(Player player, Title title) {
        LCTitleBuilder titleBuilder = LCTitleBuilder.of(title.getTitle(), TitleType.TITLE).displayFor(Duration.ofSeconds(title.getStay())).fadeInFor(Duration.ofSeconds(title.getFadeIn())).fadeOutFor(Duration.ofSeconds(title.getFadeOut()));
        LCTitleBuilder subtitleBuilder = LCTitleBuilder.of(title.getSubtitle(), TitleType.SUBTITLE).displayFor(Duration.ofSeconds(title.getStay())).fadeInFor(Duration.ofSeconds(title.getFadeIn())).fadeOutFor(Duration.ofSeconds(title.getFadeOut()));
        Arrays.asList(titleBuilder, subtitleBuilder).forEach(builder -> builder.sendAndBuild(new Player[]{player}));
    }

    public boolean isClient(Player player) {
        return LunarClientAPI.getInstance().isRunningLunarClient(player);
    }
}

