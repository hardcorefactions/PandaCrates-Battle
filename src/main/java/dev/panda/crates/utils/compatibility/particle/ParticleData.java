package dev.panda.crates.utils.compatibility.particle;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Objects;

public interface ParticleData {

    static ParticleData of(ItemStack item) {
        Objects.requireNonNull(item, "item cannot be null");
        return new AbstractParticleData(item);
    }

    static BlockData createBlockData(Material material) {
        return createBlockData(material, (byte) 0);
    }

    static BlockData createBlockData(Material material, byte legacyData) {
        Objects.requireNonNull(material, "material cannot be null");
        return new BlockData(new MaterialData(material, legacyData));
    }

    static DustOptions createDustOptions(Color color, float size) {
        Objects.requireNonNull(color, "color cannot be null");
        // Spigot 1.8.8 doesn't have a specific Dust API, so we simulate it.
        return new DustOptions(new SimulatedDust(color, size));
    }

    @Getter
    class AbstractParticleData implements ParticleData {
        final Object data;

        protected AbstractParticleData(Object data) {
            this.data = data;
        }

    }

    class BlockData extends AbstractParticleData {
        private BlockData(MaterialData data) {
            super(data);
        }

        public MaterialData getMaterialData() {
            return (MaterialData) data;
        }
    }

    class DustOptions extends AbstractParticleData {
        DustOptions(SimulatedDust dust) {
            super(dust);
        }

        public SimulatedDust getDust() {
            return (SimulatedDust) data;
        }
    }

    /**
     * Simulated class for DustOptions since Spigot 1.8.8 doesn't support DustTransition.
     */
    @Getter
    class SimulatedDust {
        private final Color color;
        private final float size;

        public SimulatedDust(Color color, float size) {
            this.color = color;
            this.size = size;
        }

    }
}