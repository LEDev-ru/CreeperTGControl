package ru.ledev.creepertgcontrol.bot.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.ledev.creepertgcontrol.bot.Bot;
import ru.ledev.creepertgcontrol.bot.BotCommand;
import ru.ledev.creepertgcontrol.utils.tps.TPSUtil;

public class PlayerCommand implements BotCommand {
    @Override
    public String getName() {
        return "player";
    }

    @Override
    public void execute(Bot bot, long chatID, String argument) {

        Server server = Bukkit.getServer();
        Player player = server.getPlayer(argument);

        if (player == null) {
            SendMessage message = new SendMessage();
            message.setChatId(chatID);
            message.setParseMode("html");
            message.setText("❌ Игрок не найден!\n" +
                    "💬 Использование: /player (ник)");
            try {
                bot.execute(message);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatID);
        message.setParseMode("html");

        String gameMode = player.getGameMode() == GameMode.SURVIVAL ? "Выживание" :
                (player.getGameMode() == GameMode.CREATIVE ? "Творческий" :
                        (player.getGameMode() == GameMode.SPECTATOR ? "Налюдатель" : "Приключения"));
        Location location = player.getLocation();

        message.setText("📄 Информация об игроке:\n" +
                "\n" +
                "📕 Имя игрока: <b>" + player.getName() + "</b>\n" +
                "😎 Есть OP: <b>" + (player.isOp() ? "Да" : "Нет") + "</b>\n" +
                "🗺 Координаты: <b>" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() +
                " (мир: " + location.getWorld().getName() + ")</b>\n" +
                "🔲 Режим игры: <b>" + gameMode + "</b>\n" +
                "✈ Летает: <b>" + (player.isFlying() ? "Да" : "Нет") + "</b>\n" +
                "❤ Здоровье: <b>" + Math.round(player.getHealth()) + "/" + Math.round(player.getMaxHealth()) + "</b>\n" +
                "🍗 Еда: <b>" + player.getFoodLevel() + "/20</b>\n" +
                "🧪 Уровень: <b>" + player.getLevel() + "</b>\n" +
                "🌐 IP: <b>" + player.getAddress().getHostString() + "</b>\n" +
                "⌛ Пинг: <i>Скоро..</i>");
        try {
            bot.execute(message);
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
