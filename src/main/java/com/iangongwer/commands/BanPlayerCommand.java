package com.iangongwer.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class BanPlayerCommand implements CommandExecutor {

	Util u = Util.getInstance();

	public static ArrayList<UUID> bannedPlayers = new ArrayList<UUID>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("banplayer") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /banplayer (player)", "c"));
			}
			if (args.length == 1) {
				if (Bukkit.getPlayer(args[0]) != null) {
					UUID playerUUID = Bukkit.getPlayer(args[0]).getUniqueId();
					if (bannedPlayers.contains(playerUUID)) {
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is already banned", "c"));
						return true;
					} else {
						bannedPlayers.add(playerUUID);
						Bukkit.getPlayer(playerUUID).kickPlayer(u.messageFormat("You are currently banned.", "c"));
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is now banned", "a"));
						return true;
					}
				} else {
					UUID playerUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
					if (bannedPlayers.contains(playerUUID)) {
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is already banned", "c"));
						return true;
					} else {
						bannedPlayers.add(playerUUID);
						player.sendMessage(Util.getInstance().messageFormat(args[0] + " is now banned", "a"));
						return true;
					}
				}
			}
		}
		return true;
	}
}