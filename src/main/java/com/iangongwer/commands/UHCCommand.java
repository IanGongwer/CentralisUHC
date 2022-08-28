package com.iangongwer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.utils.Util;

public class UHCCommand implements CommandExecutor {

	Util u = Util.getInstance();
	ConnectionMYSQL cm = ConnectionMYSQL.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("uhc") && sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(Util.getInstance().messageFormat("", "a"));
			player.sendMessage(Util.getInstance().messageFormat("--- UHC Config ---", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Final Heal: 10 minutes", "a"));
			player.sendMessage(Util.getInstance().messageFormat("PvP Enables: 15 minutes", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Meetup: 50 minutes", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Border Size: 2000x2000", "a"));
			player.sendMessage(Util.getInstance().messageFormat("F5 Abuse: Use at own risk", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Golden Heads: 2 hearts & Speed", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Absorption: Enabled", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Trapping: Allowed till 50x50", "a"));
			player.sendMessage(Util.getInstance().messageFormat("CrossTeaming: Not allowed", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Horses: Enabled, unless horseless", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Nether: Disabled", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Bookshelves: Enabled", "a"));
			player.sendMessage(Util.getInstance().messageFormat("Pearl Damage: Enabled", "a"));
			player.sendMessage(Util.getInstance().messageFormat("", "a"));
		}
		return true;
	}
}