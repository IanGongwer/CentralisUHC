package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.utils.Util;

public class PracticeCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("practice") && sender instanceof Player) {
			Player player = (Player) sender;
			UUID playerUUID = player.getUniqueId();
			if (u.isLobby()) {
				if (u.isPracticePlayer(playerUUID)) {
					u.removePracticePlayer(playerUUID);
				} else {
					u.addPracticePlayer(playerUUID);
				}
			}
		}
		return true;
	}

}