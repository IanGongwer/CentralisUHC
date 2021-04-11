package me.centy.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class PracticeCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("practice") && sender instanceof Player) {
			Player player = (Player) sender;
			if (Util.getInstance().isLobby()) {
				if (Util.getInstance().isPracticePlayer(player.getUniqueId())) {
					Util.getInstance().removePracticePlayer(player.getUniqueId());
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.teleport(GameManager.getInstance().getLobbySpawnPoint());
					player.sendMessage(Util.getInstance()
							.messageFormat("You have left practice mode. Use /prac to join practice mode.", "c"));
				} else {
					Util.getInstance().addPracticePlayer(player.getUniqueId());
					Util.getInstance().practiceInventory(player);
					player.teleport(Util.getInstance().getPracticeSpawnPoint());
					player.sendMessage(Util.getInstance()
							.messageFormat("You are now in practice mode. Use /prac to leave practice mode.", "a"));
				}
			}
		}
		return true;
	}

}