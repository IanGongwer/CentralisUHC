package com.iangongwer.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class LateScatterCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("latescatter") && sender instanceof Player) {
			Player player = (Player) sender;
			if (GameState.isInGame()) {
				if (GameRunnable.getSecondsPassed() <= 600) {
					if (!u.isInStaffMode(player.getUniqueId())) {
						if (!gm.getPlayers().contains(player.getUniqueId()) && !GameManager.getInstance()
								.getAlreadyScatteredPlayers().contains(player.getUniqueId())) {
							if (tm.areTeamsEnabled()) {
								if (tm.hasTeam(player.getUniqueId())) {
									Location memberLocation = Bukkit
											.getPlayer(tm.getTeamMembers(player.getUniqueId()).get(0)).getLocation();
									memberLocation.setY(memberLocation.getY() + 1);
									player.teleport(memberLocation);
								}

								if (!tm.hasTeam(player.getUniqueId())) {
									Location scatterLocation = gm
											.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
									scatterLocation.setY(scatterLocation.getY() + 1);
									player.teleport(scatterLocation);
									tm.createTeam(player.getUniqueId());
								}
							} else {
								Location scatterLocation = gm
										.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
								scatterLocation.setY(scatterLocation.getY() + 1);
								player.teleport(scatterLocation);
							}

							gm.setPlayerKills(player.getUniqueId(), 0);
							gm.playerScatterUtil(player);
							gm.addPlayer(player.getUniqueId());
							gm.removeSpectator(player.getUniqueId());
							player.setGameMode(GameMode.SURVIVAL);
							Bukkit.broadcastMessage(
									u.messageFormat(player.getDisplayName() + " has been latescattered.", "a"));
							player.sendMessage(u.messageFormat("You can kill sheep for string.", "c"));
							player.sendMessage(u.messageFormat(
									"Create a novice sword this game using this recipe: http://centralis.cc/recipes.html",
									"c"));
						} else {
							player.sendMessage(u.messageFormat("You are already scattered.", "c"));
						}
					} else {
						player.sendMessage(
								u.messageFormat("You are in staff mode. Use /staff to remove staff mode.", "c"));
					}
				} else {
					player.sendMessage(u.messageFormat("The game is too far in progress to join.", "c"));
				}
			} else {
				player.sendMessage(u.messageFormat("The game is not in progress.", "c"));
			}
		}
		return true;
	}
}