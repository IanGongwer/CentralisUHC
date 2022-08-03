package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class UnBanPlayerCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("unbanplayer") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /unbanplayer (player)", "c"));
			}
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					UUID playerUUID = Bukkit.getPlayer(args[0]).getUniqueId();
					if (BanPlayerCommand.bannedPlayers.contains(playerUUID)) {
						BanPlayerCommand.bannedPlayers.remove(playerUUID);
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is now unbanned", "a"));
						return true;
					} else {
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is not banned", "c"));
						return true;
					}
				} else {
					UUID playerUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
					if (BanPlayerCommand.bannedPlayers.contains(playerUUID)) {
						BanPlayerCommand.bannedPlayers.remove(playerUUID);
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is now unbanned", "a"));
						return true;
					} else {
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is not banned", "c"));
						return true;
					}
				}
			}
		}
		return true;
	}
}