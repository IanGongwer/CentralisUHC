package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class TeamCoordsCommand implements CommandExecutor {

	Util u = Util.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("teamcoords") && sender instanceof Player) {
			Player player = (Player) sender;
			if (tm.areTeamsEnabled()) {
				if (tm.hasTeam(player.getUniqueId())) {
					if (args.length == 0) {
						for (UUID member : tm.getTeamMembers(player.getUniqueId())) {
							if (tm.getDeceasedMembers(player.getUniqueId()).contains(player.getUniqueId())) {
								player.sendMessage(u.messageFormat("You can not send coords while spectating.", "c"));
							} else {
								int x = player.getLocation().getBlockX();
								int y = player.getLocation().getBlockY();
								int z = player.getLocation().getBlockZ();
								Bukkit.getPlayer(member).sendMessage(u.messageFormat(
										"[Coords] (" + player.getDisplayName() + ") X: " + x + " Y: " + y + " Z: " + z,
										"f"));
							}
						}
					} else {
						player.sendMessage(u.messageFormat("/teamcoords", "c"));
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

}