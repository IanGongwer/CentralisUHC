package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.Util;

public class MuteChatCommand implements CommandExecutor {

	Util u = Util.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mutechat") && sender instanceof Player) {
			if (ChatUtil.isChatMuted()) {
				ChatUtil.setChatMute(false);
				Bukkit.broadcastMessage(u.messageFormat("Chat is now unmuted.", "a"));
			} else {
				ChatUtil.setChatMute(true);
				Bukkit.broadcastMessage(u.messageFormat("Chat is now muted.", "c"));
			}
		}
		return true;
	}

}