package me.centy.uhc.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.Util;

public class WhitelistCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whitelist") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0 || args.length > 2) {
				player.sendMessage(Util.getInstance()
						.messageFormat("Usage: /whitelist (toggle/current/add/remove) (player)", "c"));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("toggle")) {
					if (Util.getInstance().getWhitelistStatus()) {
						Util.getInstance().setWhitelistStatus(false);
						player.sendMessage(Util.getInstance().messageFormat("The whitelist is now off.", "a"));
						return true;
					} else {
						Util.getInstance().setWhitelistStatus(true);
						player.sendMessage(Util.getInstance().messageFormat("The whitelist is now on.", "a"));
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("current")) {
					player.sendMessage(Util.getInstance()
							.messageFormat("" + Util.getInstance().getWhitelistedPlayersNames(), "a"));
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("add")) {
					UUID playerUUID = Bukkit.getPlayer(args[1]).getUniqueId();
					Util.getInstance().addWhitelistedPlayer(playerUUID);
					player.sendMessage(
							Util.getInstance().messageFormat("You have added " + args[1] + " to the whitelist.", "a"));
				}
				if (args[0].equalsIgnoreCase("remove")) {
					UUID playerUUID = Bukkit.getPlayer(args[1]).getUniqueId();
					Util.getInstance().removeWhitelistedPlayer(playerUUID);
					player.sendMessage(Util.getInstance()
							.messageFormat("You have removed " + args[1] + " from the whitelist.", "a"));

				}
			}
		}
		return true;
	}
}