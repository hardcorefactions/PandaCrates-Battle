/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.ConversationContext
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.conversations.StringPrompt
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package dev.panda.crates.crate.prompt;

import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.menus.key.EditKeyLoreLinesMenu;
import dev.panda.crates.utils.CC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CrateKeyLoreAddLinePrompt
extends StringPrompt {
    private final Crate crate;
    private final int index;

    public CrateKeyLoreAddLinePrompt(Crate crate, int index) {
        this.crate = crate;
        this.index = index;
    }

    @NotNull
    public String getPromptText(@NotNull ConversationContext conversationContext) {
        return CC.YELLOW + "Type in the line you want to add to the key lore, type " + CC.RED + "cancel " + CC.YELLOW + "to cancel.";
    }

    @Nullable
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String s) {
        if (s == null) {
            return this;
        }
        Player player = (Player)context.getForWhom();
        if (s.equalsIgnoreCase("cancel")) {
            player.sendMessage(CC.RED + "You've canceled the operation.");
            return END_OF_CONVERSATION;
        }
        List<String> lines = this.crate.getKeyLoreLines();
        if (this.index == lines.size() - 1 || lines.isEmpty()) {
            this.crate.getKeyLoreLines().add(CC.translate(s));
        } else {
            this.replaceLine(lines, s);
        }
        player.sendMessage(CC.GREEN + "You've added the line " + CC.YELLOW + CC.translate(s) + CC.GREEN + " to the key lore.");
        EditKeyLoreLinesMenu menu = new EditKeyLoreLinesMenu(this.crate);
        menu.selected = this.crate.getKeyLoreLines().size() > this.index ? this.index + 1 : this.index;
        menu.open(player);
        return END_OF_CONVERSATION;
    }

    private void replaceLine(List<String> lines, String s) {
        int i;
        ArrayList<String> newList = new ArrayList<>();
        for (i = 0; i <= this.index; ++i) {
            newList.add(lines.get(i));
        }
        newList.add(CC.translate(s));
        for (i = this.index + 1; i < lines.size(); ++i) {
            String line = lines.get(i);
            newList.add(line);
        }
        lines.clear();
        lines.addAll(newList);
    }
}

