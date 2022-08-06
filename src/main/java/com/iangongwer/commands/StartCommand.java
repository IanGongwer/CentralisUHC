package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.Util;

public class StartCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("start") && sender instanceof Player) {
			Player player = (Player) sender;
			if (GameState.isLobby()) {
				if (gm.getPredeterminedLocations().size() != 0) {
					player.sendMessage(u.messageFormat("You have started the scattering process.", "a"));
					ChatUtil.setChatMute(true);
					gm.scatterPlayers(gm.getPlayers());
				} else {
					player.sendMessage(u.messageFormat("You have not used /calculate yet.", "c"));
				}
			} else {
				player.sendMessage(u.messageFormat("The game is currently in progress.", "c"));
			}
		}
		return true;
	}

}