package com.iangongwer.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.utils.Util;

public class HelpopCommand implements CommandExecutor {

	Util u = Util.getInstance();
	ConnectionMYSQL cm = ConnectionMYSQL.getInstance();

	static ArrayList<UUID> muteHelpOPPlayers = new ArrayList<UUID>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("helpop") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(u.messageFormat("Usage: /helpop (message)", "c"));
			}
			if (args.length == 2) {
				if (player.hasPermission("uhc.staff")) {
					if (args[0].equalsIgnoreCase("mute")) {
						if (!muteHelpOPPlayers.contains(Bukkit.getPlayer(args[1]).getUniqueId())) {
							muteHelpOPPlayers.add(Bukkit.getPlayer(args[1]).getUniqueId());
							player.sendMessage(u.messageFormat("[HELPOP] " + args[1] + " is now helpop muted", "a"));
							return true;
						} else {
							muteHelpOPPlayers.remove(Bukkit.getPlayer(args[1]).getUniqueId());
							player.sendMessage(u.messageFormat("[HELPOP] " + args[1] + " is now helpop unmuted", "a"));
							return true;
						}
					}
				}
			}
			if (args.length > 0) {
				if (!muteHelpOPPlayers.contains(player.getUniqueId())) {
					String message = "";
					for (String part : args) {
						if (message != "")
							message += " ";
						message += part;
					}
					for (UUID staffUUID : Util.getStaffMode()) {
						Bukkit.getPlayer(staffUUID)
								.sendMessage(
										u.messageFormat("[HELPOP] " + player.getDisplayName() + ": " + message, "c"));
					}
					player.sendMessage(u.messageFormat("[HELPOP] Your helpop message has been sent", "a"));
				} else {
					player.sendMessage(u.messageFormat("[HELPOP] You are currently helpop muted", "c"));
				}
			}
		}
		return true;
	}
}