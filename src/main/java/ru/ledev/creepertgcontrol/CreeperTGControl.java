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

    public Bot bot;

    @Override
    public void onEnable() {

        UptimeUtil.start();

        instance = this;

        saveDefaultConfig();

        TPSUtil.startTPSTimer();

        bot = new Bot(getConfig().getString("bot-username"), getConfig().getString("bot-token"));
        registerBot();

        getCommand("creepertgcontrol").setExecutor(new TGControlCmd());
        getCommand("creepertgcontrol").setTabCompleter(new TGControlCmd());

        if (getConfig().getBoolean("server-start-msg")) bot.sendServerStartMsg();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {

        if (getConfig().getBoolean("server-stop-msg")) bot.sendServerStopMsg();

        getLogger().info("Plugin disabled!");
    }

    private void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static CreeperTGControl getInstance() {
        return instance;
    }
}
