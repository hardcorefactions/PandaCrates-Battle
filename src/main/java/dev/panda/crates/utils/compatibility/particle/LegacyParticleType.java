/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 *
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 */
package dev.panda.crates.utils.compatibility.particle;

import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class LegacyParticleType
implements ParticleType {
    static final Class<?> ENUM_PARTICLE = ParticleReflection.nmsOptionalClass("EnumParticle").orElse(null);
    private static final Map<String, String> LEGACY_NAMES = Collections.unmodifiableMap(LegacyParticleType.getLegacyParticleNames());
    private static final int[] EMPTY_DATA = new int[0];
    private static final boolean IS_1_8 = ENUM_PARTICLE != null;
    private static final Method WORLD_GET_HANDLE;
    private static final Method WORLD_SEND_PARTICLE;
    private static final Constructor<?> PACKET_PARTICLE;
    private static final Method PLAYER_GET_HANDLE;
    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;
    private final String name;
    private final Object particle;

    LegacyParticleType(String name, Object particle) {
        this.name = Objects.requireNonNull(name, "name");
        this.particle = Objects.requireNonNull(particle, "particle");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?> getRawDataType() {
        switch (this.name) {
            case "ITEM_CRACK": {
                return ItemStack.class;
            }
            case "BLOCK_CRACK":
            case "BLOCK_DUST":
            case "FALLING_DUST": {
                return MaterialData.class;
            }
            case "REDSTONE": {
                return Color.class;
            }
        }
        return Void.class;
    }

    public void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object rawData) {
        try {
            if (rawData instanceof ParticleData.AbstractParticleData) {
                rawData = ((ParticleData.AbstractParticleData)rawData).data;
            }
            int[] data = this.toData(rawData);
            Object worldServer = WORLD_GET_HANDLE.invoke(world);
            if (rawData instanceof Color && this.getRawDataType() == Color.class) {
                Color color = (Color)rawData;
                count = 0;
                offsetX = ParticleTypes.color(color.getRed());
                offsetY = ParticleTypes.color(color.getGreen());
                offsetZ = ParticleTypes.color(color.getBlue());
                extra = 1.0;
            }
            if (IS_1_8) {
                WORLD_SEND_PARTICLE.invoke(worldServer, null, this.particle, true, x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
            } else {
                String particleName = this.particle + (data.length != 2 ? "" : "_" + data[0] + "_" + data[1]);
                WORLD_SEND_PARTICLE.invoke(worldServer, particleName, x, y, z, count, offsetX, offsetY, offsetZ, extra);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, Object rawData) {
        try {
            Object packet;
            if (rawData instanceof ParticleData.AbstractParticleData) {
                rawData = ((ParticleData.AbstractParticleData)rawData).data;
            }
            int[] data = this.toData(rawData);
            if (rawData instanceof Color && this.getRawDataType() == Color.class) {
                Color color = (Color)rawData;
                count = 0;
                offsetX = ParticleTypes.color(color.getRed());
                offsetY = ParticleTypes.color(color.getGreen());
                offsetZ = ParticleTypes.color(color.getBlue());
                extra = 1.0;
            }
            if (IS_1_8) {
                packet = PACKET_PARTICLE.newInstance(this.particle, true, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count, data);
            } else {
                String particleName = this.particle + (data.length != 2 ? "" : "_" + data[0] + "_" + data[1]);
                packet = PACKET_PARTICLE.newInstance(particleName, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
            }
            Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
            Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] toData(Object data) {
        Class<?> dataType = this.getRawDataType();
        if (dataType == ItemStack.class) {
            if (!(data instanceof ItemStack)) {
                int[] nArray;
                if (IS_1_8) {
                    nArray = new int[2];
                } else {
                    int[] nArray2 = new int[2];
                    nArray2[0] = 1;
                    nArray = nArray2;
                    nArray2[1] = 0;
                }
                return nArray;
            }
            ItemStack itemStack = (ItemStack)data;
            return new int[]{itemStack.getType().getId(), itemStack.getDurability()};
        }
        if (dataType == MaterialData.class) {
            if (!(data instanceof MaterialData)) {
                int[] nArray;
                if (IS_1_8) {
                    nArray = new int[1];
                } else {
                    int[] nArray3 = new int[2];
                    nArray3[0] = 1;
                    nArray = nArray3;
                    nArray3[1] = 0;
                }
                return nArray;
            }
            MaterialData materialData = (MaterialData)data;
            if (IS_1_8) {
                return new int[]{materialData.getItemType().getId() + (materialData.getData() << 12)};
            }
            return new int[]{materialData.getItemType().getId(), materialData.getData()};
        }
        return EMPTY_DATA;
    }

    static ParticleType of(String name) {
        if (IS_1_8) {
            return new LegacyParticleType(name, ParticleReflection.enumValueOf(ENUM_PARTICLE, name));
        }
        String legacyName = LEGACY_NAMES.get(name);
        if (legacyName == null) {
            throw new IllegalArgumentException("Invalid legacy particle: " + name);
        }
        return new LegacyParticleType(name, legacyName);
    }

    private static Map<String, String> getLegacyParticleNames() {
        HashMap<String, String> legacyNames = new HashMap<>(64);
        legacyNames.put("EXPLOSION_NORMAL", "explode");
        legacyNames.put("EXPLOSION_LARGE", "largeexplode");
        legacyNames.put("EXPLOSION_HUGE", "hugeexplosion");
        legacyNames.put("FIREWORKS_SPARK", "fireworksSpark");
        legacyNames.put("WATER_BUBBLE", "bubble");
        legacyNames.put("WATER_SPLASH", "splash");
        legacyNames.put("WATER_WAKE", "wake");
        legacyNames.put("SUSPENDED", "suspended");
        legacyNames.put("SUSPENDED_DEPTH", "depthsuspend");
        legacyNames.put("CRIT", "crit");
        legacyNames.put("CRIT_MAGIC", "magicCrit");
        legacyNames.put("SMOKE_NORMAL", "smoke");
        legacyNames.put("SMOKE_LARGE", "largesmoke");
        legacyNames.put("SPELL", "spell");
        legacyNames.put("SPELL_INSTANT", "instantSpell");
        legacyNames.put("SPELL_MOB", "mobSpell");
        legacyNames.put("SPELL_MOB_AMBIENT", "mobSpellAmbient");
        legacyNames.put("SPELL_WITCH", "witchMagic");
        legacyNames.put("DRIP_WATER", "dripWater");
        legacyNames.put("DRIP_LAVA", "dripLava");
        legacyNames.put("VILLAGER_ANGRY", "angryVillager");
        legacyNames.put("VILLAGER_HAPPY", "happyVillager");
        legacyNames.put("TOWN_AURA", "townaura");
        legacyNames.put("NOTE", "note");
        legacyNames.put("PORTAL", "portal");
        legacyNames.put("ENCHANTMENT_TABLE", "enchantmenttable");
        legacyNames.put("FLAME", "flame");
        legacyNames.put("LAVA", "lava");
        legacyNames.put("CLOUD", "cloud");
        legacyNames.put("REDSTONE", "reddust");
        legacyNames.put("SNOWBALL", "snowballpoof");
        legacyNames.put("SNOW_SHOVEL", "snowshovel");
        legacyNames.put("SLIME", "slime");
        legacyNames.put("HEART", "heart");
        legacyNames.put("ITEM_CRACK", "iconcrack");
        legacyNames.put("BLOCK_CRACK", "blockcrack");
        legacyNames.put("BLOCK_DUST", "blockdust");
        return legacyNames;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegacyParticleType)) {
            return false;
        }
        LegacyParticleType particleType = (LegacyParticleType)o;
        return this.particle == particleType.particle;
    }

    public int hashCode() {
        return this.particle.hashCode();
    }

    public String toString() {
        return "LegacyParticleType{particle=" + this.particle + '}';
    }

    static {
        try {
            Class<?> packetParticleClass = ParticleReflection.nmsClass("PacketPlayOutWorldParticles");
            Class<?> playerClass = ParticleReflection.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = ParticleReflection.nmsClass("PlayerConnection");
            Class<?> worldClass = ParticleReflection.nmsClass("WorldServer");
            Class<?> entityPlayerClass = ParticleReflection.nmsClass("EntityPlayer");
            Class<?> craftPlayerClass = ParticleReflection.obcClass("entity.CraftPlayer");
            Class<?> craftWorldClass = ParticleReflection.obcClass("CraftWorld");
            if (IS_1_8) {
                PACKET_PARTICLE = packetParticleClass.getConstructor(ENUM_PARTICLE, Boolean.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("sendParticles", entityPlayerClass, ENUM_PARTICLE, Boolean.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, int[].class);
            } else {
                PACKET_PARTICLE = packetParticleClass.getConstructor(String.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE);
                WORLD_SEND_PARTICLE = worldClass.getDeclaredMethod("a", String.class, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
            }
            WORLD_GET_HANDLE = craftWorldClass.getDeclaredMethod("getHandle");
            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = playerClass.getField("playerConnection");
            SEND_PACKET = playerConnectionClass.getMethod("sendPacket", ParticleReflection.nmsClass("Packet"));
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

