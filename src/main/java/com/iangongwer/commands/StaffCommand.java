package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class StaffCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staff") && sender instanceof Player) {
			Player player = (Player) sender;
			if (LobbyUtil.isPracticePlayer(player.getUniqueId())) {
				player.sendMessage(u.messageFormat("You are currently in practice. Use /prac to leave", "c"));
			} else {
				if (!u.isInStaffMode(player.getUniqueId())) {
					u.addStaffMode(player.getUniqueId());
				} else {
					u.removeStaffMode(player.getUniqueId());
				}
			}
		}
		return true;
	}
}