package me.centy.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.Util;

public class MuteChatCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mutechat") && sender instanceof Player) {
			if (Util.getInstance().isChatMuted()) {
				Util.getInstance().setChatMute(false);
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Chat is now unmuted.", "a"));
			} else {
				Util.getInstance().setChatMute(true);
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Chat is now muted.", "c"));
			}
		}
		return true;
	}

}