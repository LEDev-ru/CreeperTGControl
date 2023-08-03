package ru.ledev.creepertgcontrol.utils.tps;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import ru.ledev.creepertgcontrol.CreeperTGControl;

public class TPSUtil {

    private static TPSTimer timer;

    public static String getTPS() {
        return String.format("%.1f",timer.get());
    }

    public static void startTPSTimer() {
        CreeperTGControl plugin = CreeperTGControl.getInstance();
        timer = new TPSTimer();
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                plugin, timer, 100L, 50L
        );
    }
}