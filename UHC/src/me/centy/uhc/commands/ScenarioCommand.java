package me.centy.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class ScenarioCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("scenario") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length < 1 || args.length > 2) {
				player.sendMessage(
						Util.getInstance().messageFormat("Usage: /scenario (scenario/list) (enable/disable)", "c"));
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("list")) {
					player.sendMessage(Util.getInstance()
							.messageFormat(GameManager.getInstance().getActiveScenarios().toString(), "a"));
				} else {
					player.sendMessage(
							Util.getInstance().messageFormat("Usage: /scenario (scenario/list) (enable/disable)", "c"));
				}
			}
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("enable")) {
					if (!GameManager.getInstance().isScenarioActive(args[0])) {
						GameManager.getInstance().enableScenario(args[0].toUpperCase());
						player.sendMessage(
								Util.getInstance().messageFormat(args[0].toUpperCase() + " is now enabled.", "a"));
					} else {
						player.sendMessage(
								Util.getInstance().messageFormat(args[0].toUpperCase() + " is already enabled.", "c"));
					}
				}
				if (args[1].equalsIgnoreCase("disable")) {
					if (GameManager.getInstance().isScenarioActive(args[0])) {
						GameManager.getInstance().disableScenario(args[0].toUpperCase());
						player.sendMessage(
								Util.getInstance().messageFormat(args[0].toUpperCase() + " is now disabled.", "c"));
					} else {
						player.sendMessage(
								Util.getInstance().messageFormat(args[0].toUpperCase() + " is already disabled.", "c"));
					}
				}
			}
		}
		return true;
	}
}