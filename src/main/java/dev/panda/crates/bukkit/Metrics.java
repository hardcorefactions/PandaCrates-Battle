/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package dev.panda.crates.bukkit;

import dev.panda.crates.MetricsBase;
import dev.panda.crates.charts.CustomChart;
import dev.panda.crates.json.JsonObjectBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;

public class Metrics {
    private final Plugin plugin;
    private final MetricsBase metricsBase;

    public Metrics(JavaPlugin plugin, int serviceId) {
        this.plugin = plugin;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            config.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        boolean enabled = config.getBoolean("enabled", true);
        String serverUUID = config.getString("serverUuid");
        boolean logErrors = config.getBoolean("logFailedRequests", false);
        boolean logSentData = config.getBoolean("logSentData", false);
        boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        this.metricsBase = new MetricsBase("bukkit", serverUUID, serviceId, enabled, this::appendPlatformData, this::appendServiceData, submitDataTask -> Bukkit.getScheduler().runTask(plugin, submitDataTask), () -> plugin.isEnabled(), (message, error) -> this.plugin.getLogger().log(Level.WARNING, message, error), message -> this.plugin.getLogger().log(Level.INFO, message), logErrors, logSentData, logResponseStatusText);
    }

    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        builder.appendField("playerAmount", this.getPlayerAmount());
        builder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", Bukkit.getVersion());
        builder.appendField("bukkitName", Bukkit.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getDescription().getVersion());
    }

    private int getPlayerAmount() {
        try {
            Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            return onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).length;
        } catch (Exception e) {
            return Bukkit.getOnlinePlayers().size();
        }
    }
}

