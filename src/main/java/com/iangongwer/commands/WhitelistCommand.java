package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class WhitelistCommand implements CommandExecutor {

	Util u = Util.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whitelist") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1 && !args[0].equalsIgnoreCase("toggle") && !args[0].equalsIgnoreCase("current")) {
				player.sendMessage(u.messageFormat("Usage: /whitelist (toggle/current)", "c"));
			}
			if (args.length == 0 || args.length > 1) {
				player.sendMessage(u.messageFormat("Usage: /whitelist (toggle/current)", "c"));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("toggle")) {
					if (u.getWhitelistStatus()) {
						u.setWhitelistStatus(false);
						player.sendMessage(u.messageFormat("The whitelist is now off.", "a"));
						return true;
					} else {
						u.setWhitelistStatus(true);
						player.sendMessage(u.messageFormat("The whitelist is now on.", "a"));
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("current")) {
					player.sendMessage(u.messageFormat("" + u.getWhitelistedPlayersUUID(), "a"));
				}
			}
		}
		return true;
	}
}