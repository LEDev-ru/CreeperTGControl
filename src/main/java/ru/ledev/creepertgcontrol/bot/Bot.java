package ru.ledev.creepertgcontrol.bot;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ledev.creepertgcontrol.CreeperTGControl;
import ru.ledev.creepertgcontrol.bot.commands.InfoCommand;
import ru.ledev.creepertgcontrol.bot.commands.ListCommand;
import ru.ledev.creepertgcontrol.bot.commands.PlayerCommand;

import java.util.Arrays;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private final String username;
    private final String token;

    private final List<BotCommand> commands = Arrays.asList(
            new InfoCommand(),
            new ListCommand(),
            new PlayerCommand()
    );

    public Bot(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public void sendServerStartMsg() {
        for (long id : CreeperTGControl.getInstance().getConfig().getLongList("admins-ids")) {
            SendMessage message = new SendMessage();
            message.setChatId(id);
            message.setText("🌐 Сервер запущен!");
            try {
                execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendServerStopMsg() {
        for (long id : CreeperTGControl.getInstance().getConfig().getLongList("admins-ids")) {
            SendMessage message = new SendMessage();
            message.setChatId(id);
            message.setText("🌐 Сервер выключен!");
            try {
                execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            long chatID = update.getMessage().getChatId();

            if (!CreeperTGControl.getInstance().getConfig().getLongList("admins-ids").contains(chatID)) {
                SendMessage message = new SendMessage();
                message.setChatId(chatID);
                message.setText("Вас нет в списке админов! Ваш ID: " + chatID);
                try {
                    execute(message);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }

            String messageText = update.getMessage().getText();

            if (!messageText.startsWith("/")) {

                Server server = Bukkit.getServer();

                server.dispatchCommand(server.getConsoleSender(), messageText);

                SendMessage message = new SendMessage();
                message.setChatId(chatID);
                message.setText("Команда отправлена в консоль: " + messageText);
                try {
                    execute(message);
                }
                catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            }

            for (BotCommand command : commands) {
                if (messageText.startsWith("/" + command.getName())) {
                    command.execute(this, chatID, messageText.replaceFirst("/" + command.getName() + " ", ""));
                    return;
                }
            }

            // Help
            SendMessage message = new SendMessage();
            message.setChatId(chatID);
            message.setParseMode("html");
            message.setText("ℹ Помощь:\n" +
                    "\n" +
                    "(команда) - отправить команду в консоль\n" +
                    "/help - помощь\n" +
                    "/info - информация сервера\n" +
                    "/list - список игроков\n" +
                    "/player (ник) - информация об игроке");
            try {
                execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
