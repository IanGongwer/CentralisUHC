package me.centy.uhc.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class CalculateCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("calculate") && sender instanceof Player) {
			Player player = (Player) sender;
			Util.getInstance().setWhitelistStatus(true);
			for (UUID allPlayers : GameManager.getInstance().getPlayers()) {
				Location scatterLocation = GameManager.getInstance()
						.checkLocationEligibilityNoTeleport(GameManager.getInstance().makeScatterLocation());
				GameManager.predeterminedLocations.put(allPlayers, scatterLocation);
				GameManager.getInstance().setPlayerKills(allPlayers, 0);
			}
			if (GameManager.predeterminedLocations.size() == GameManager.getInstance().getPlayers().size()) {
				player.sendMessage(Util.getInstance().messageFormat("All locations calculated.", "a"));
			}
		}
		return true;
	}

}