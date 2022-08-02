package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.utils.Util;

public class StatsCommand implements CommandExecutor {

	Util u = Util.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("stats") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 1) {
				player.sendMessage(u.messageFormat("/stats <player>", "c"));
			}
			if (args.length == 0) {
				player.sendMessage(u.messageFormat("Statistics for " + player.getDisplayName(), "a"));

				if (!Main.isRedisEnabled()) {
					player.sendMessage(u.messageFormat("Wins: " + dbm.getWins(player.getUniqueId()), "a"));
					player.sendMessage(u.messageFormat("Kills: " + dbm.getKills(player.getUniqueId()), "a"));
					player.sendMessage(u.messageFormat("Deaths: " + dbm.getDeaths(player.getUniqueId()), "a"));
				} else {
					player.sendMessage(u.messageFormat("Wins: " + cr.getWins(player.getUniqueId()), "a"));
					player.sendMessage(u.messageFormat("Kills: " + cr.getKills(player.getUniqueId()), "a"));
					player.sendMessage(u.messageFormat("Deaths: " + cr.getDeaths(player.getUniqueId()), "a"));
				}

				player.sendMessage(u.messageFormat(
						"To view the all-time leaderboard, please visit our website at http://centralis.cc/stats.html",
						"a"));
			}
			if (args.length == 1) {
				Player player2 = Bukkit.getPlayer(args[0]);
				if (player2 != null) {
					player.sendMessage(u.messageFormat("Statistics for " + player2.getName(), "a"));

					if (!Main.isRedisEnabled()) {
						player.sendMessage(u.messageFormat("Wins: " + dbm.getWins(player2.getUniqueId()), "a"));
						player.sendMessage(u.messageFormat("Kills: " + dbm.getKills(player2.getUniqueId()), "a"));
						player.sendMessage(u.messageFormat("Deaths: " + dbm.getDeaths(player2.getUniqueId()), "a"));
					} else {
						player.sendMessage(u.messageFormat("Wins: " + cr.getWins(player2.getUniqueId()), "a"));
						player.sendMessage(u.messageFormat("Kills: " + cr.getKills(player2.getUniqueId()), "a"));
						player.sendMessage(u.messageFormat("Deaths: " + cr.getDeaths(player2.getUniqueId()), "a"));
					}
				} else {
					player.sendMessage(u.messageFormat(args[0] + " is not online", "c"));
				}
				player.sendMessage(u.messageFormat(
						"To view the all-time leaderboard, please visit our website at http://centralis.cc/stats.html",
						"a"));
			}
		}
		return true;
	}

}