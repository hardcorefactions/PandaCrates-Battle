/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package dev.panda.crates.utils;

import dev.panda.crates.loader.CratesLoader;
import dev.panda.crates.managers.hologram.title.Title;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TitleUtil {
    private static final String OG_PACKET_TITLE_NAME = "PacketPlayOutTitle";
    private static final String PACKET_TIMES_NAME = "network.protocol.game.ClientboundSetTitlesAnimationPacket";
    private static final String PACKET_TITLE_TEXT_NAME = "network.protocol.game.ClientboundSetTitleTextPacket";
    private static final String PACKET_SUBTITLE_TEXT_NAME = "network.protocol.game.ClientboundSetSubtitleTextPacket";
    private static final String CHAT_BASE_COMPONENT_NAME = "network.chat.IChatBaseComponent";
    private static final String CRAFT_CHAT_MESSAGE_NAME = "util.CraftChatMessage";

    public static void sendTitle(CratesLoader loader, Player player, Title title) {
        if (ServerUtil.SERVER_VERSION_INT <= 7) {
            if (!loader.isLunarHookEnabled()) {
                return;
            }
            loader.getLunarHook().sendTitle(player, title);
            return;
        }
        if (loader.isLunar(player)) {
            loader.getLunarHook().sendTitle(player, title);
        } else {
            try {
                Method sendTitleMethod = player.getClass().getDeclaredMethod("sendTitle", String.class, String.class);
                sendTitleMethod.invoke(player, title.getTitle(), title.getSubtitle());
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

