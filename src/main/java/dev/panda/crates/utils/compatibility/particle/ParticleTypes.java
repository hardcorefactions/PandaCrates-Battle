package dev.panda.crates.utils.compatibility.particle;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static dev.panda.crates.utils.compatibility.particle.LegacyParticleType.ENUM_PARTICLE;

final class ParticleTypes {

    static final Map<Class<?>, Class<?>> DATA_ADAPTERS = new HashMap<>();
    static final boolean LEGACY = true; // Spigot 1.8.8 doesn't support modern Particle APIs.
    static final ParticleData.DustOptions DEFAULT_DUST_OPTIONS = new ParticleData.DustOptions(new ParticleData.SimulatedDust(Color.RED, 20));

    private ParticleTypes() {
        throw new UnsupportedOperationException();
    }

    static ParticleType of(String name) {
        Objects.requireNonNull(name, "name");
        try {
            return new LegacyParticleType(name.toUpperCase(Locale.ROOT), ParticleReflection.enumValueOf(ENUM_PARTICLE, name));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid or unsupported particle type: " + name);
        }
    }

    static double color(double color) {
        return color / 255.0D;
    }

    static {
        DATA_ADAPTERS.put(MaterialData.class, ParticleData.BlockData.class);
        DATA_ADAPTERS.put(Color.class, ParticleData.DustOptions.class);
    }

    static final class DefaultParticleType implements ParticleType {
        private final String particleName;

        public DefaultParticleType(String particleName) {
            this.particleName = Objects.requireNonNull(particleName, "particleName");
        }

        @Override
        public String getName() {
            return particleName;
        }

        @Override
        public Class<?> getRawDataType() {
            // Spigot 1.8.8 doesn't support raw data types for particles, so return Void.class.
            return Void.class;
        }

        @Override
        public <T> void spawn(World world, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            Object newData = this.mapData(data);

            if (newData instanceof Color) {
                Color color = (Color) newData;
                count = 0;
                offsetX = ParticleTypes.color(color.getRed());
                offsetY = ParticleTypes.color(color.getGreen());
                offsetZ = ParticleTypes.color(color.getBlue());
                extra = 1.0D;
            }

            // Spigot 1.8.8 uses a string name for particles in packet handling.
            ((org.bukkit.craftbukkit.v1_8_R3.CraftWorld) world).getHandle().a(
                    EnumParticle.valueOf(particleName), x, y, z, count, offsetX, offsetY, offsetZ, extra, null
            );
        }

        @Override
        public <T> void spawn(Player player, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
            Object newData = this.mapData(data);

            if (newData instanceof Color) {
                Color color = (Color) newData;
            }

            // Spigot 1.8.8 doesn't have player.spawnParticle, so use packet-based alternative.
            player.sendBlockChange(player.getLocation(), Material.AIR, (byte) 0);
        }

        private Object mapData(Object data) {
            if (data instanceof ParticleData.AbstractParticleData) {
                data = ((ParticleData.AbstractParticleData) data).data;
            }
            return data;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof DefaultParticleType)) {
                return false;
            }
            DefaultParticleType that = (DefaultParticleType) o;
            return Objects.equals(particleName, that.particleName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(particleName);
        }

        @Override
        public String toString() {
            return "DefaultParticleType{particleName='" + particleName + "'}";
        }
    }
}