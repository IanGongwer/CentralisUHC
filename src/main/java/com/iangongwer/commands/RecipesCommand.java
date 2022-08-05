package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.utils.Util;

public class RecipesCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("recipes") && sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(Util.getInstance()
					.messageFormat("List of Recipes Link: http://centralis.cc/recipes.html", "a"));
		}
		return true;
	}
}