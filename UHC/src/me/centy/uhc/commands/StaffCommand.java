package me.centy.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class StaffCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staff") && sender instanceof Player) {
			Player player = (Player) sender;
			if (Util.getInstance().isPracticePlayer(player.getUniqueId())) {
				player.sendMessage(
						Util.getInstance().messageFormat("You are currently in practice. Use /prac to leave", "c"));
			}
			if (!Util.getInstance().isPracticePlayer(player.getUniqueId())) {
				if (!Util.getInstance().isInStaffMode(player.getUniqueId())) {
					GameManager.getInstance().removePlayer(player.getUniqueId());
					GameManager.getInstance().removeSpectator(player.getUniqueId());
					Util.getInstance().addStaffMode(player.getUniqueId());
					player.setGameMode(GameMode.SPECTATOR);
					for (Player allPlayers : Bukkit.getOnlinePlayers()) {
						allPlayers.hidePlayer(player);
					}
					player.sendMessage(Util.getInstance().messageFormat("You are now in staff mode.", "a"));
				} else {
					Util.getInstance().removeStaffMode(player.getUniqueId());
					if (Util.getInstance().isInGame()) {
						GameManager.getInstance().addSpectator(player.getUniqueId());
						player.setGameMode(GameMode.SPECTATOR);
					}
					if (Util.getInstance().isLobby()) {
						GameManager.getInstance().addPlayer(player.getUniqueId());
						for (Player allPlayers : Bukkit.getOnlinePlayers()) {
							allPlayers.showPlayer(player);
						}
						player.setGameMode(GameMode.SURVIVAL);
					}
					player.sendMessage(Util.getInstance().messageFormat("You are now not in staff mode.", "c"));
				}
			}
		}
		return true;
	}
}