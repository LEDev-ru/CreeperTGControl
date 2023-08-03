package ru.ledev.creepertgcontrol.bot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ledev.creepertgcontrol.bot.Bot;
import ru.ledev.creepertgcontrol.bot.BotCommand;

public class ListCommand implements BotCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(Bot bot, long chatID, String argument) {
        Server server = Bukkit.getServer();

        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setParseMode("html");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("📄 Список игроков:\n");
        int n = 0;
        for (Player player : server.getOnlinePlayers()) {
            n++;
            stringBuilder.append("\n");
            stringBuilder.append(n);
            stringBuilder.append(". ");
            if (player.isOp()) stringBuilder.append("<b>[OP]</b> ");
            stringBuilder.append(player.getName());
        }
        message.setText(stringBuilder.toString());
        try {
            bot.execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
