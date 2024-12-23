/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.utils;

import dev.panda.crates.Crates;
import dev.panda.crates.loader.CratesLoader;

public final class StringUtils {
    public static final String RIGHT_LITTLE_ARROW = "▸";

    public static String repeat(String var0, int var1) {
        if (var0 == null) {
            return null;
        }
        if (var1 <= 0) {
            return "";
        }
        int var2 = var0.length();
        if (var1 != 1 && var2 != 0) {
            if (var2 == 1 && var1 <= 8192) {
                return StringUtils.repeat(var0.charAt(0), var1);
            }
            int var3 = var2 * var1;
            switch (var2) {
                case 1: {
                    return StringUtils.repeat(var0.charAt(0), var1);
                }
                case 2: {
                    char var4 = var0.charAt(0);
                    char var5 = var0.charAt(1);
                    char[] var6 = new char[var3];
                    for (int var7 = var1 * 2 - 2; var7 >= 0; --var7) {
                        var6[var7] = var4;
                        var6[var7 + 1] = var5;
                        --var7;
                    }
                    return new String(var6);
                }
            }
            StringBuilder var9 = new StringBuilder(var3);
            for (int var8 = 0; var8 < var1; ++var8) {
                var9.append(var0);
            }
            return var9.toString();
        }
        return var0;
    }

    public static String repeat(char var0, int var1) {
        char[] var2 = new char[var1];
        for (int var3 = var1 - 1; var3 >= 0; --var3) {
            var2[var3] = var0;
        }
        return new String(var2);
    }

    public static String capitalize(Enum<?> enu) {
        return StringUtils.capitalize(enu.name().toLowerCase());
    }

    public static String capitalize(String str) {
        int codepoint;
        int newCodePoint;
        int strLen;
        int n = strLen = str == null ? 0 : str.length();
        if (strLen == 0) {
            return str;
        }
        int firstCodepoint = str.codePointAt(0);
        if (firstCodepoint == (newCodePoint = Character.toTitleCase(firstCodepoint))) {
            return str;
        }
        int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint;
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; inOffset += Character.charCount(codepoint)) {
            codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint;
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String getNiceEnumName(Enum<?> eenum) {
        return StringUtils.capitalizeFully(eenum.name());
    }

    public static String capitalizeFully(String name) {
        if (name.contains("_") || name.contains(" ")) {
            String[] split;
            StringBuilder builder = new StringBuilder();
            for (String str : name.split("_")) {
                builder.append(StringUtils.capitalize(str.toLowerCase())).append(" ");
            }
            String full = builder.toString();
            return full.substring(0, full.length() - 1);
        }
        return StringUtils.capitalize(name.toLowerCase());
    }

    public static String metricData(Crates plugin, CratesLoader loader) {
        return loader.getMainConfig().getString("license");
    }

    public static String metricInfoV2() {
        return ServerUtil.getIP();
    }

    private StringUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

