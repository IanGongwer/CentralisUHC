package com.iangongwer.commands;

import java.util.Calendar;
import java.util.TimeZone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameState;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class ScheduleCommand implements CommandExecutor {

	Util u = Util.getInstance();
	ConnectionMYSQL cm = ConnectionMYSQL.getInstance();

	static TimeZone timeZone = TimeZone.getTimeZone("UTC");
	public static Calendar cal = Calendar.getInstance(timeZone);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("schedule") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length != 5) {
				player.sendMessage(
						Util.getInstance().messageFormat(
								"Usage: /schedule (yyyy) (# of month - 1) (# of day in month) (hour of day in military) (minute of hour)",
								"c"));
			}
			if (args.length == 5) {
				if (GameState.isLobby()) {
					if (LobbyUtil.isPracticePlayer(player.getUniqueId())) {
						player.sendMessage(u.messageFormat("You are currently in practice. Use /prac to leave", "c"));
					} else {
						u.addStaffMode(player.getUniqueId());
						cal.set(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]),
								Integer.valueOf(args[3]),
								Integer.valueOf(args[4]));
						cal.set(Calendar.SECOND, 0);
						player.sendMessage(
								Util.getInstance().messageFormat("The game start time is now set to: " + cal.getTime(),
										"a"));
					}
				} else {
					player.sendMessage(
							Util.getInstance().messageFormat("You cannot schedule a game right now", "c"));
				}
			}
		}
		return true;
	}
}