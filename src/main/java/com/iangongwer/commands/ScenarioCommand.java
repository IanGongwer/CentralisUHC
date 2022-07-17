package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.utils.Util;

public class ScenarioCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("scenario") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1 || args.length > 2) {
				player.sendMessage(u.messageFormat("Usage: /scenario (scenario/list) (enable/disable)", "c"));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					player.sendMessage(u.messageFormat(gm.getActiveScenarios().toString(), "a"));
				} else {
					player.sendMessage(u.messageFormat("Usage: /scenario (scenario/list) (enable/disable)", "c"));
				}
			}
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("enable")) {
					if (!gm.isScenarioActive(args[0])) {
						gm.enableScenario(args[0].toUpperCase());
						player.sendMessage(u.messageFormat(args[0].toUpperCase() + " is now enabled.", "a"));
					} else {
						player.sendMessage(u.messageFormat(args[0].toUpperCase() + " is already enabled.", "c"));
					}
				}
				if (args[1].equalsIgnoreCase("disable")) {
					if (gm.isScenarioActive(args[0])) {
						gm.disableScenario(args[0].toUpperCase());
						player.sendMessage(u.messageFormat(args[0].toUpperCase() + " is now disabled.", "c"));
					} else {
						player.sendMessage(u.messageFormat(args[0].toUpperCase() + " is already disabled.", "c"));
					}
				}
			}
		}
		return true;
	}
}