package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class TeamChatCommand implements CommandExecutor {

	Util u = Util.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("teamchat") && sender instanceof Player) {
			Player player = (Player) sender;
			if (tm.areTeamsEnabled()) {
				if (tm.hasTeam(player.getUniqueId())) {
					if (args.length >= 1) {
						String message = getMessage(args);
						for (UUID member : tm.getTeamMembers(player.getUniqueId())) {
							if (tm.getDeceasedMembers(player.getUniqueId()).contains(player.getUniqueId())) {
								player.sendMessage(u.messageFormat("You can not use team chat while spectating.", "c"));
							} else {
								Bukkit.getPlayer(member).sendMessage(
										u.messageFormat("[TC] " + player.getDisplayName() + ": " + message, "f"));
							}
						}
					} else {
						player.sendMessage(u.messageFormat("/teamchat (message)", "c"));
					}
				} else {
					player.sendMessage(
							u.messageFormat("You are not in a team. Use /team create to generate a team.", "c"));
				}
			} else {
				player.sendMessage(u.messageFormat("Teams are currently disabled.", "c"));
			}
		}
		return true;
	}

	public String getMessage(String[] args) {
		String message = "";
		for (int i = 0; i < args.length; i++) {
			message += args[i] + " ";
		}
		message = message.trim();
		return message;
	}

}