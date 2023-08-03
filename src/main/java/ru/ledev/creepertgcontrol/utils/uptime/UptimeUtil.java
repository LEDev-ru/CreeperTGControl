package ru.ledev.creepertgcontrol.utils.uptime;

public class UptimeUtil {

    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static long getUptime() {
        return System.currentTimeMillis() - startTime;
    }

}
