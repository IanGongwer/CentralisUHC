package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class BroadcastCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("broadcast") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /broadcast (message)", "c"));
			}
			if (args.length > 0) {
				String message = "";
				for (String part : args) {
					if (message != "")
						message += " ";
					message += part;
				}
				Bukkit.broadcastMessage(Util.getInstance().messageFormat(message + ".", "a"));
			}
		}
		return true;
	}
}