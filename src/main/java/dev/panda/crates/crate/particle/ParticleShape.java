// Decompiled with: FernFlower
// Class Version: 8
package dev.panda.crates.crate.particle;

import dev.panda.crates.utils.MathUtil;
import dev.panda.crates.utils.StringUtils;
import dev.panda.crates.utils.compatibility.material.CompatibleMaterial;
import dev.panda.crates.utils.compatibility.particle.ParticleType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

@Getter
public enum ParticleShape {
    SPIRAL(ChatColor.GREEN, Material.EMERALD_ORE),
    SQUARE(ChatColor.GOLD, Material.HAY_BLOCK),
    CIRCLE(ChatColor.WHITE, CompatibleMaterial.SNOW_BALL.getMaterial());

    private final ChatColor color;
    private final Material material;

    public Runnable play(final ParticleType particle, final Location location) {
        switch(this) {
            case CIRCLE:
                return new Runnable() {
                    final List<Location> loc = MathUtil.getCircle(location.clone().add(0.5D, 0.5D, 0.5D), 1.0F, 20);
                    int integer = 0;

                    public void run() {
                        if (this.integer == this.loc.size()) {
                            this.integer = 0;
                        }

                        particle.spawn(location.getWorld(), this.loc.get(this.integer++), 1, 0.0D, 0.0D, 0.0D, 5.0D);
                    }
                };
            case SPIRAL:
                return new Runnable() {
                    final Location loc = location.clone().add(0.5D, 0.5D, 0.5D);
                    int stepX = 0;
                    final int particles = 6;
                    final int particlesPerRotation = 50;
                    final double radius = 0.8D;

                    public void run() {
                        for(double stepY = -60.0D; stepY < 60.0D; stepY += 120.0D / 6.0D) {
                            double dx = -MathUtil.cos(((double)this.stepX + stepY) / 50.0D * Math.PI * 2.0D) * 0.8D;
                            double dy = stepY / 50.0D / 2.0D;
                            double dz = -MathUtil.sin(((double)this.stepX + stepY) / 50.0D * Math.PI * 2.0D) * 0.8D;
                            particle.spawn(location.getWorld(), this.loc.clone().add(dx, dy, dz), 1, 0.0D, 0.0D, 0.0D, 4.0D);
                        }

                        ++this.stepX;
                    }
                };
            case SQUARE:
                return new Runnable() {
                    final Location loc = location.clone().add(1.24D, 0.5D, 1.12D);
                    int integer = 0;

                    public void run() {
                        if (this.integer >= 0 && this.integer < 12) {
                            this.loc.subtract(0.12D, 0.0D, 0.0D);
                        } else if (this.integer > 11 && this.integer < 23) {
                            this.loc.subtract(0.0D, 0.0D, 0.12D);
                        } else if (this.integer > 22 && this.integer < 34) {
                            this.loc.add(0.12D, 0.0D, 0.0D);
                        } else if (this.integer > 33 && this.integer < 45) {
                            this.loc.add(0.0D, 0.0D, 0.12D);
                        }

                        if (this.integer == 44) {
                            this.integer = 0;
                        }

                        ++this.integer;
                        particle.spawn(location.getWorld(), this.loc, 1, 0.0D, 0.0D, 0.0D, 5.0D);
                    }
                };
            default:
                return null;
        }
    }

    public String getNiceName() {
        return StringUtils.capitalize(this);
    }

    public ParticleShape next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    ParticleShape(ChatColor color, Material material) {
        this.color = color;
        this.material = material;
    }

    // $FF: synthetic method
    private static ParticleShape[] $values() {
        return new ParticleShape[]{SPIRAL, SQUARE, CIRCLE};
    }
}