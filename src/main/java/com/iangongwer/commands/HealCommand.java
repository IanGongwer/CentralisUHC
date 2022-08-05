package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class HealCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("heal") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getPlayer(args[0]);
					targetPlayer.setHealth(20);
					player.sendMessage(Util.getInstance()
							.messageFormat("You have healed " + args[0], "a"));
					targetPlayer.sendMessage(Util.getInstance()
							.messageFormat("You have been healed", "a"));
				} else {
					player.sendMessage(Util.getInstance()
							.messageFormat("That player is not online", "c"));
				}
			} else {
				player.sendMessage(Util.getInstance()
						.messageFormat("Usage: /heal (player)", "c"));
			}
		}
		return true;
	}
}