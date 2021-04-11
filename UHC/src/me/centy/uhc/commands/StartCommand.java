package me.centy.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class StartCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("start") && sender instanceof Player) {
			Player player = (Player) sender;
			if (Util.getInstance().isLobby()) {
				if (GameManager.predeterminedLocations.size() != 0) {
					player.sendMessage(
							Util.getInstance().messageFormat("You have started the scattering process.", "a"));
					GameManager.getInstance().scatterPlayers(GameManager.getInstance().getPlayers());
				} else {
					player.sendMessage(Util.getInstance().messageFormat("You have not used /calculate yet.", "c"));
				}
			} else {
				player.sendMessage(Util.getInstance().messageFormat("The game is currently in progress.", "c"));
			}
		}
		return true;
	}

}