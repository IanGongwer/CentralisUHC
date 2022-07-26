package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.holograms.LobbyHolograms;
import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.utils.Util;

public class DatabaseCommand implements CommandExecutor {

	ConnectionRedis cr = ConnectionRedis.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	LobbyHolograms lh = new LobbyHolograms();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("database") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0 || args.length == 1 || args.length > 2) {
				player.sendMessage(Util.getInstance().messageFormat("Usage: /database (mysql/redis) toggle", "c"));
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("redis") && args[1].equalsIgnoreCase("toggle")) {
					if (!Main.isRedisEnabled()) {
						dbm.disconnect();
						cr.connectToRedis();
						lh.createLobbyHologram();
						if(cr.isConnectedSuccessfully()) {
							player.sendMessage(Util.getInstance().messageFormat("Redis is now enabled", "a"));
						} else {
							player.sendMessage(Util.getInstance().messageFormat("Could not connect to Redis", "c"));
						}
						return true;
					} else {
						player.sendMessage(Util.getInstance().messageFormat("Redis is already enabled", "c"));
					}
				}
				if (args[0].equalsIgnoreCase("mysql") && args[1].equalsIgnoreCase("toggle")) {
					if (Main.isRedisEnabled()) {
					cr.closePool();
					dbm.connect();
					lh.createLobbyHologram();
					if(dbm.isConnectedSuccessfully()) {
						player.sendMessage(Util.getInstance().messageFormat("MySQL is now enabled", "a"));
					} else {
						player.sendMessage(Util.getInstance().messageFormat("Could not connect to MySQL", "c"));
					}
					return true;
					} else {
						player.sendMessage(Util.getInstance().messageFormat("MySQL is already enabled", "c"));
					}
				}
			}
		}
		return true;
	}
}