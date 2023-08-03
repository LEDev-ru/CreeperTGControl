package ru.ledev.creepertgcontrol.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import ru.ledev.creepertgcontrol.CreeperTGControl;

import java.util.Collections;
import java.util.List;

public class TGControlCmd implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            CreeperTGControl.getInstance().reloadConfig();
            sender.sendMessage("Список админов обновлён!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) return Collections.singletonList("reload");

        return Collections.emptyList();
    }
}
