/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package dev.panda.crates.crate.menus;

import com.google.common.collect.Maps;
import dev.panda.crates.Crates;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.particle.ParticleEffect;
import dev.panda.crates.crate.particle.ParticleShape;
import dev.panda.crates.utils.CC;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.compatibility.particle.ParticleType;
import dev.panda.crates.utils.item.ItemBuilder;
import dev.panda.crates.utils.menu.Button;
import dev.panda.crates.utils.menu.Menu;
import dev.panda.crates.utils.menu.buttons.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectEffectMenu
extends Menu {
    private final Crates plugin;
    private final Crate crate;

    @Override
    public String getTitle(Player player) {
        return "Select Particle Effect";
    }

    @Override
    public int size(Map<Integer, Button> m) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = Maps.newHashMap();
        AtomicInteger slot = new AtomicInteger();
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&c&lHeart Effect", CompatibleMaterial.INK_SACK.getMaterial(), 1, ParticleType.of("HEART")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&a&lHappy Villager Effect", Material.EMERALD, 0, ParticleType.of("VILLAGER_HAPPY")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&f&lSnowball Effect", CompatibleMaterial.SNOW_BALL.getMaterial(), 0, ParticleType.of("SNOWBALL")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&b&lNotes Effect", Material.NOTE_BLOCK, 0, ParticleType.of("NOTE")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&d&lColors Effect", CompatibleMaterial.INK_SACK.getMaterial(), 9, ParticleType.of("REDSTONE")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&a&lSlime Effect", Material.SLIME_BALL, 0, ParticleType.of("SLIME")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&9&lWater Effect", Material.WATER_BUCKET, 0, ParticleType.of("DRIP_WATER")));
        buttons.put(slot.getAndIncrement(), new ParticleButton(this.plugin, this.crate, "&6&lLava Effect", Material.LAVA_BUCKET, 0, ParticleType.of("DRIP_LAVA")));
        buttons.put(slot.getAndIncrement(), Button.fromItem(new ItemBuilder(Material.REDSTONE_BLOCK).name(CC.RED + CC.BOLD + "Reset Effect").lore("&7Click to reset the effect.").build(), clicker -> {
            if (this.crate.getEffect() == null) {
                player.sendMessage(CC.RED + "The crate doesn't have an effect.");
                return;
            }
            this.crate.getEffect().stop();
            this.crate.setEffect(null);
            clicker.sendMessage(CC.GREEN + "You've reset the effect of crate " + this.crate.getDisplayName() + CC.GREEN + ".");
        }));
        buttons.put(this.size(buttons) - 5, new BackButton(new CrateEditMenu(this.crate)));
        return buttons;
    }

    public SelectEffectMenu(Crates plugin, Crate crate) {
        this.plugin = plugin;
        this.crate = crate;
    }

    private static class ParticleButton
    extends Button {
        private final Crate crate;
        private final String displayName;
        private final Material material;
        private final int data;
        private final ParticleType particle;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.material, this.data).name(this.displayName).lore(" ", "&7Left-Click to select the shape.").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            ParticleButton.playNeutral(player);
            new SelectShapeMenu(this.crate, this.particle).openMenu(player);
        }

        public ParticleButton(Crates plugin, Crate crate, String displayName, Material material, int data, ParticleType particle) {
            this.crate = crate;
            this.displayName = displayName;
            this.material = material;
            this.data = data;
            this.particle = particle;
        }
    }

    private static class ModifyShapeButton
    extends Button {
        private final Crate crate;
        private final ParticleType type;
        private ParticleShape shape;

        public ModifyShapeButton(Crate crate, ParticleType type) {
            this.crate = crate;
            this.type = type;
            if (crate.getEffect() != null) {
                this.shape = crate.getEffect().getParticle().getName().equals(type.getName()) ? crate.getEffect().getShape() : null;
            }
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER).name(CC.BLUE + "Shape");
            ArrayList<String> list = new ArrayList<>();
            list.add(CC.CHAT_BAR);
            list.add(CC.GRAY + "Here you can select between");
            list.add(CC.GRAY + "multiple shapes: ");
            list.add(" ");
            if (this.shape == null) {
                list.add(CC.GREEN + " " + "▸" + " " + CC.BLUE + "None");
            }
            for (ParticleShape other : ParticleShape.values()) {
                boolean current = this.shape != null && this.shape == other;
                list.add(CC.GREEN + " " + "▸" + " " + (current ? CC.BLUE : CC.YELLOW) + other.getNiceName());
            }
            list.add(CC.CHAT_BAR);
            builder.lore(list);
            return builder.build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            ModifyShapeButton.playSuccess(player);
            ParticleShape particleShape = this.shape = this.shape == null ? ParticleShape.SPIRAL : this.shape.next();
            if (this.crate.getEffect() != null) {
                this.crate.getEffect().stop();
            }
            ParticleEffect effect = new ParticleEffect(this.type, this.shape);
            this.crate.setEffect(effect);
            effect.safePlay(this.crate.getChestLocation());
            player.sendMessage(CC.GREEN + "You've selected the shape " + this.shape.getNiceName() + " for crate " + this.crate.getDisplayName() + CC.GREEN + ".");
        }
    }

    private static class SelectShapeMenu
    extends Menu {
        private final Crate crate;
        private final ParticleType type;

        @Override
        public int size(Map<Integer, Button> buttons) {
            return 27;
        }

        @Override
        public String getTitle(Player player) {
            return "Select Particle Shape";
        }

        @Override
        public Map<Integer, Button> getButtons(Player player) {
            HashMap<Integer, Button> buttons = new HashMap<>();
            buttons.put(13, new ModifyShapeButton(this.crate, this.type));
            return buttons;
        }

        public SelectShapeMenu(Crate crate, ParticleType type) {
            this.crate = crate;
            this.type = type;
        }
    }
}

