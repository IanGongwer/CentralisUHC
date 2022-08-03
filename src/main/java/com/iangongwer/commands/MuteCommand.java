package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.Util;

public class MuteCommand implements CommandExecutor {

	Util u = Util.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0 || args.length > 1) {
				player.sendMessage(u.messageFormat("Usage: /mute (player)", "c"));
			}
			if (args.length == 1) {
				Player mutedPlayer = Bukkit.getPlayer(args[0]);
				if (mutedPlayer.isOnline()) {
					if (ChatUtil.isMuted(player.getUniqueId())) {
						ChatUtil.removeMutedPlayer(player.getUniqueId());
						player.sendMessage(u.messageFormat("You have unmuted " + args[0] + ".", "a"));
						mutedPlayer.sendMessage(u.messageFormat("You are now unmuted.", "a"));
					} else {
						ChatUtil.addMutedPlayer(player.getUniqueId());
						player.sendMessage(u.messageFormat("You have muted " + args[0] + ".", "c"));
						mutedPlayer.sendMessage(u.messageFormat("You are now muted.", "c"));
					}
				} else {
					player.sendMessage(u.messageFormat(args[0] + " is not online.", "c"));
				}
			}
		}

		return true;
	}
}