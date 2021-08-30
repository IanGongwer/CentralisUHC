package me.centy.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.Util;

public class TeamCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("team") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /team (create)", "c"));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("create")) {
				}
			}
		}
		return true;
	}
}