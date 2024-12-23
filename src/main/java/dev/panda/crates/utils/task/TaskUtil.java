/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package dev.panda.crates.utils.task;

import dev.panda.crates.Crates;
import org.bukkit.scheduler.BukkitRunnable;

public final class TaskUtil {
    public static void run(Runnable runnable) {
        Crates.getInstance().getServer().getScheduler().runTask(Crates.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Crates.getInstance().getServer().getScheduler().runTaskTimer(Crates.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Crates.getInstance(), delay, timer);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        Crates.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Crates.getInstance(), runnable, delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(Crates.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Crates.getInstance().getServer().getScheduler().runTaskLater(Crates.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        try {
            Crates.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Crates.getInstance(), runnable, delay);
        } catch (IllegalStateException e) {
            Crates.getInstance().getServer().getScheduler().runTaskLater(Crates.getInstance(), runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runTaskTimerAsynchronously(Runnable runnable, int delay) {
        try {
            Crates.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Crates.getInstance(), runnable, 20L * (long)delay, 20L * (long)delay);
        } catch (IllegalStateException e) {
            Crates.getInstance().getServer().getScheduler().runTaskTimer(Crates.getInstance(), runnable, 20L * (long)delay, 20L * (long)delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void runAsync(Runnable runnable) {
        try {
            Crates.getInstance().getServer().getScheduler().runTaskAsynchronously(Crates.getInstance(), runnable);
        } catch (IllegalStateException e) {
            Crates.getInstance().getServer().getScheduler().runTask(Crates.getInstance(), runnable);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private TaskUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

