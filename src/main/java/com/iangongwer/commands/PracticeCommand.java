package com.iangongwer.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class PracticeCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("practice") && sender instanceof Player) {
			Player player = (Player) sender;
			UUID playerUUID = player.getUniqueId();
			if (GameState.isLobby()) {
				if (LobbyUtil.isPracticePlayer(playerUUID)) {
					LobbyUtil.removePracticePlayer(playerUUID);
				} else {
					LobbyUtil.addPracticePlayer(playerUUID);
				}
			}
		}
		return true;
	}

}