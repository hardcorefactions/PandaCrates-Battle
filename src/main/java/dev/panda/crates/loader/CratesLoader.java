/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.plugin.Plugin
 */
package dev.panda.crates.loader;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import dev.panda.crates.Crates;
import dev.panda.crates.airdrop.command.AirDropCommand;
import dev.panda.crates.bukkit.Metrics;
import dev.panda.crates.charts.SimplePie;
import dev.panda.crates.crate.Crate;
import dev.panda.crates.crate.commands.CrateCommands;
import dev.panda.crates.crate.commands.GiveKeyCommand;
import dev.panda.crates.crate.commands.KeyAllCommand;
import dev.panda.crates.crate.commands.PandaCommand;
import dev.panda.crates.crate.listeners.CrateListener;
import dev.panda.crates.crate.provider.CrateProvider;
import dev.panda.crates.hologram.HologramListener;
import dev.panda.crates.hook.LunarClientHook;
import dev.panda.crates.managers.hologram.NMSManager;
import dev.panda.crates.mystery.command.MysteryBoxCommand;
import dev.panda.crates.reward.RewardsListeners;
import dev.panda.crates.utils.*;
import dev.panda.crates.utils.fancy.FormatingMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CratesLoader {
    private final Crates plugin;
    private final NMSManager NMSManager;
    private final ConfigFile mainConfig;
    private final boolean lunarHookEnabled;
    private LunarClientHook lunarHook;

    public CratesLoader(Crates plugin) {
        this.plugin = plugin;
        this.mainConfig = new ConfigFile(plugin, "config.yml");
        this.NMSManager = new NMSManager();
        plugin.setServerLoaded(true);
        Crate.load(this);
        new CrateListener(plugin);
        new RewardsListeners(plugin, true);
        new HologramListener(plugin, true);
        this.lunarHookEnabled = Bukkit.getPluginManager().getPlugin("LunarClientAPI") != null;
        if (this.lunarHookEnabled) {
            this.hookLunar();
        }
        this.loadMetrics();
        this.registerCommands(plugin, true);
    }

    private void hookLunar() {
        this.lunarHook = new LunarClientHook();
    }

    public List<String> getDefaultHologramLines(Crate crate) {
        return this.mainConfig.getStringList("crate.hologram.default-lines").stream().map(CC::translate).collect(Collectors.toList());
    }

    public List<String> getDefaultKeyLoreLines(Crate crate) {
        return this.mainConfig.getStringList("crate.key.default-lore").stream().map(CC::translate).collect(Collectors.toList());
    }

    private void registerCommands(Crates plugin, boolean isValid) {
        if (!isValid) {
            return;
        }
        if (plugin.isServerLoaded()) {
            CommandService service = Drink.get(plugin);
            service.bind(Crate.class).toProvider(new CrateProvider());
            service.register(new AirDropCommand(), "airdrop");
            service.register(new MysteryBoxCommand(), "mysterybox");
            service.register(new CrateCommands(), "crate", "cr", "crates");
            service.register(new GiveKeyCommand(), "givekey");
            service.register(new KeyAllCommand(), "keyall");
            service.register(new PandaCommand(), "panda", "pandaver");
            service.registerCommands();
        }
        Bukkit.getLogger().info("Command registered");
    }

    private void loadMetrics() {
        int pluginId = 16892;
        Metrics metrics = new Metrics(this.plugin, pluginId);
        metrics.addCustomChart(new SimplePie("IP", ServerUtil::getIP));
    }

    public boolean isLunar(Player player) {
        return this.lunarHookEnabled && this.lunarHook != null && this.lunarHook.isClient(player);
    }

}

