package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class DiscordCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("discord") && sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(Util.getInstance()
					.messageFormat("Discord Link: https://discord.gg/YTEM7Dq", "a"));
		}
		return true;
	}
}