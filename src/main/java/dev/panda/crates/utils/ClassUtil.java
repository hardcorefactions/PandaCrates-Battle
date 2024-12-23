/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dev.panda.crates.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {
    public static Collection<Class<?>> getClassesInPackage(JavaPlugin plugin, String packageName) {
        JarFile jarFile;
        List<Class<?>> classes = new ArrayList<>();
        CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ").replace("%5B", "[").replace("%5D", "]").replace("%7B", "{").replace("%7D", "}");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className == null) continue;
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz == null) continue;
            classes.add(clazz);
        }
        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public static boolean hasInterface(Class<?> clazz, Class<?> interf) {
        for (Class<?> intf : clazz.getInterfaces()) {
            if (intf != interf) continue;
            return true;
        }
        return false;
    }

    private ClassUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

