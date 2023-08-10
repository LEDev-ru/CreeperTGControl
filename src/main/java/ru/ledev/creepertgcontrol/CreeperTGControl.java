package ru.ledev.creepertgcontrol;

import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.ledev.creepertgcontrol.bot.Bot;
import ru.ledev.creepertgcontrol.commands.TGControlCmd;
import ru.ledev.creepertgcontrol.utils.tps.TPSUtil;
import ru.ledev.creepertgcontrol.utils.uptime.UptimeUtil;

public final class CreeperTGControl extends JavaPlugin {

    private static CreeperTGControl instance;

    private static Bot bot;
    private static boolean botRegistered = false;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        UptimeUtil.start();
        TPSUtil.start();

        bot = new Bot(getConfig().getString("bot-username"), getConfig().getString("bot-token"));
        if (!registerBot()) {
            getLogger().warning("Error registering TG Bot! Goodbye!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        else botRegistered = true;

        getCommand("creepertgcontrol").setExecutor(new TGControlCmd());
        getCommand("creepertgcontrol").setTabCompleter(new TGControlCmd());

        if (getConfig().getBoolean("server-start-msg")) bot.sendServerStartMsg();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {

        if (getConfig().getBoolean("server-stop-msg") && botRegistered) bot.sendServerStopMsg();

        getLogger().info("Plugin disabled!");
    }

    private boolean registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static CreeperTGControl getInstance() {
        return instance;
    }
}
