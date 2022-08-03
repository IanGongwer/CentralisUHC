package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
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
				player.sendMessage(u.messageFormat("Usage: /whitelist (toggle/current/add/remove) (player)", "c"));
			}
			if (args.length == 0 || args.length > 2) {
				player.sendMessage(u.messageFormat("Usage: /whitelist (toggle/current/add/remove) (player)", "c"));
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
					player.sendMessage(u.messageFormat("" + u.getWhitelistedPlayersNames(), "a"));
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("add")) {
					if (Bukkit.getPlayer(args[1]) != null) {
						UUID playerUUID = Bukkit.getPlayer(args[1]).getUniqueId();
						if (u.getWhitelistedPlayersUUID().contains(playerUUID)) {
							player.sendMessage(u.messageFormat(args[1] + " is already whitelisted.", "c"));
						} else {
							u.addWhitelistedPlayer(playerUUID);
							player.sendMessage(
									u.messageFormat("You have added " + args[1] + " to the whitelist.", "a"));
						}
					} else {
						UUID playerUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
						if (u.getWhitelistedPlayersUUID().contains(playerUUID)) {
							player.sendMessage(u.messageFormat(args[1] + " is already whitelisted.", "c"));
						} else {
							u.addWhitelistedPlayer(playerUUID);
							player.sendMessage(
									u.messageFormat("You have added " + args[1] + " to the whitelist.", "a"));
						}
					}
				}
				if (args[0].equalsIgnoreCase("remove")) {
					if (Bukkit.getPlayer(args[1]) != null) {
						UUID playerUUID = Bukkit.getPlayer(args[1]).getUniqueId();
						if (!u.getWhitelistedPlayersUUID().contains(playerUUID)) {
							player.sendMessage(u.messageFormat(args[1] + " is not whitelisted.", "c"));
						} else {
							u.removeWhitelistedPlayer(playerUUID);
							player.sendMessage(
									u.messageFormat("You have removed " + args[1] + " from the whitelist.", "a"));
						}

					} else {
						UUID playerUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
						if (!u.getWhitelistedPlayersUUID().contains(playerUUID)) {
							player.sendMessage(u.messageFormat(args[1] + " is not whitelisted.", "c"));
						} else {
							u.removeWhitelistedPlayer(playerUUID);
							player.sendMessage(
									u.messageFormat("You have removed " + args[1] + " from the whitelist.", "a"));
						}
					}
				}
			}
		}
		return true;
	}
}