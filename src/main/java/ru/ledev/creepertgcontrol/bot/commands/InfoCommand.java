package ru.ledev.creepertgcontrol.bot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ledev.creepertgcontrol.bot.Bot;
import ru.ledev.creepertgcontrol.bot.BotCommand;
import ru.ledev.creepertgcontrol.utils.tps.TPSUtil;
import ru.ledev.creepertgcontrol.utils.uptime.UptimeUtil;

public class InfoCommand implements BotCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(Bot bot, long chatID, String argument) {
        Server server = Bukkit.getServer();

        double totalMemory = (double) Runtime.getRuntime().totalMemory() / 1048576.0;
        double usedMemory = totalMemory - ((double) Runtime.getRuntime().freeMemory() / 1048576.0);

        int uptime = (int) (UptimeUtil.getUptime() / 1000L);
        int seconds = uptime % 60;
        int minutes = (uptime / 60) % 60;
        int hours = (uptime / 3600) % 24;
        int days = uptime / 86400;

        StringBuilder uptimeStringBuilder = new StringBuilder();
        if (days > 0) {
            uptimeStringBuilder.append(days);
            uptimeStringBuilder.append(" д ");
        }
        if (hours > 0) {
            uptimeStringBuilder.append(hours);
            uptimeStringBuilder.append(" ч ");
        }
        if (minutes > 0) {
            uptimeStringBuilder.append(minutes);
            uptimeStringBuilder.append(" мин ");
        }
        uptimeStringBuilder.append(seconds);
        uptimeStringBuilder.append(" сек ");

        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setParseMode("html");
        message.setText("📄 Информация сервера:\n" +
                "\n" +
                "👨‍💻 Онлайн сервера: <b>" + server.getOnlinePlayers().size() + "/" + server.getMaxPlayers() + "</b>\n" +
                "⏳ TPS: <b>" + TPSUtil.getTPS() + "</b>\n" +
                "🕰 Аптайм: <b>" + uptimeStringBuilder + "</b>\n" +
                "💿 Использование ОЗУ: <b>" + String.format("%.1f",usedMemory) + "/" + String.format("%.1f",totalMemory) + " МБ </b>\n" +
                "🖥 Использование CPU: <i>Скоро..</i>");
        try {
            bot.execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
