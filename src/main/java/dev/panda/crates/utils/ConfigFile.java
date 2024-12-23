/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dev.panda.crates.utils;

import com.google.common.io.Files;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConfigFile
extends YamlConfiguration {
    public static final Charset UTF8_CHARSET = StandardCharsets.UTF_8;

    public ConfigFile(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load(File file) throws IOException, InvalidConfigurationException {
        if (file == null) {
            throw new IllegalArgumentException("Stream cannot be null.");
        }
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(file.toURL().openStream(), UTF8_CHARSET);
        try (BufferedReader input = new BufferedReader(reader)){
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }
        this.loadFromString(builder.toString());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save(File file) throws IOException {
        Files.createParentDirs(file);
        String data = this.saveToString();
        FileOutputStream stream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(stream, UTF8_CHARSET);
        try {
            writer.write(data);
        } finally {
            writer.flush();
            writer.close();
        }
    }
}

