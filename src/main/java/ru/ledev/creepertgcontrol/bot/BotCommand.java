package ru.ledev.creepertgcontrol.bot;

public interface BotCommand {
    String getName();
    void execute(Bot bot, long chatID, String argument);
}
