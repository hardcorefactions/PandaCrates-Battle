/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package dev.panda.crates.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public final class MathUtil {
    private static final int SIN_BITS;
    private static final int SIN_MASK;
    private static final int SIN_COUNT;
    private static final double radFull;
    private static final double radToIndex;
    private static final double degFull;
    private static final double degToIndex;
    private static final double[] sin;
    private static final double[] cos;

    public static List<Location> getCircle(Location center, float radius, int amount) {
        ArrayList<Location> list = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            double a = Math.PI * 2 / (double)amount * (double)i;
            double x = Math.cos(a) * (double)radius;
            double z = Math.sin(a) * (double)radius;
            list.add(center.clone().add(x, 0.0, z));
        }
        return list;
    }

    public static double sin(double rad) {
        return sin[(int)(rad * radToIndex) & SIN_MASK];
    }

    public static double cos(double rad) {
        return cos[(int)(rad * radToIndex) & SIN_MASK];
    }

    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        int i;
        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;
        radFull = Math.PI * 2;
        degFull = 360.0;
        radToIndex = (double)SIN_COUNT / radFull;
        degToIndex = (double)SIN_COUNT / degFull;
        sin = new double[SIN_COUNT];
        cos = new double[SIN_COUNT];
        for (i = 0; i < SIN_COUNT; ++i) {
            MathUtil.sin[i] = Math.sin((double)(((float)i + 0.5f) / (float)SIN_COUNT) * radFull);
            MathUtil.cos[i] = Math.cos((double)(((float)i + 0.5f) / (float)SIN_COUNT) * radFull);
        }
        for (i = 0; i < 360; i += 90) {
            MathUtil.sin[(int)((double)i * MathUtil.degToIndex) & MathUtil.SIN_MASK] = Math.sin((double)i * Math.PI / 180.0);
            MathUtil.cos[(int)((double)i * MathUtil.degToIndex) & MathUtil.SIN_MASK] = Math.cos((double)i * Math.PI / 180.0);
        }
    }
}

