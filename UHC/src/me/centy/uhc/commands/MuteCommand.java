package me.centy.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.Util;

public class MuteCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0 || args.length > 1) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /mute (player)", "c"));
			}
			if (args.length == 1) {
				Player mutedPlayer = Bukkit.getPlayer(args[0]);
				if (mutedPlayer.isOnline()) {
					if (Util.getInstance().isMuted(player.getUniqueId())) {
						Util.getInstance().removeMutedPlayer(player.getUniqueId());
						player.sendMessage(Util.getInstance().messageFormat("You have unmuted " + args[0] + ".", "a"));
						mutedPlayer.sendMessage(Util.getInstance().messageFormat("You are now unmuted.", "a"));
					} else {
						Util.getInstance().addMutedPlayer(player.getUniqueId());
						player.sendMessage(Util.getInstance().messageFormat("You have muted " + args[0] + ".", "c"));
						mutedPlayer.sendMessage(Util.getInstance().messageFormat("You are now muted.", "c"));
					}
				} else {
					player.sendMessage(Util.getInstance().messageFormat(args[0] + " is not online.", "c"));
				}
			}
		}

		return true;
	}
}