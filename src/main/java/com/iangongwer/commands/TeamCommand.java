package com.iangongwer.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class TeamCommand implements CommandExecutor {

	Util u = Util.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("team") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0 || args.length > 2) {
				if (!player.hasPermission("uhc.staff")) {
					if (tm.areTeamsEnabled()) {
						player.sendMessage(u.messageFormat("Usage: /team create/disband/leave/info", "c"));
						player.sendMessage(u.messageFormat("Usage: /team invite/join/kick (player)", "c"));
					}
				} else {
					player.sendMessage(u.messageFormat("Usage: /team create/disband/leave/info", "c"));
					player.sendMessage(u.messageFormat("Usage: /team invite/join/kick (player)", "c"));
					player.sendMessage(u.messageFormat("Usage: /team enable/disable", "c"));
				}
			}
			if (args.length == 1) {
				if (player.hasPermission("uhc.staff")) {
					if (args[0].equalsIgnoreCase("enable")) {
						if (tm.areTeamsEnabled()) {
							player.sendMessage(u.messageFormat("Teams are already enabled.", "c"));
						} else {
							player.sendMessage(u.messageFormat("Teams are now enabled.", "a"));
							tm.setTeamsStatus(true);
						}
					}
					if (args[0].equalsIgnoreCase("disable")) {
						if (tm.areTeamsEnabled()) {
							player.sendMessage(u.messageFormat("Teams are now disabled.", "a"));
							tm.setTeamsStatus(false);
						} else {
							player.sendMessage(u.messageFormat("Teams are already disabled.", "c"));
						}
					}
				}
			}
			if (tm.areTeamsEnabled()) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("create")) {
						if (tm.getTeam(player.getUniqueId()) == null) {
							tm.createTeam(player.getUniqueId());
							player.sendMessage(
									u.messageFormat("You have created Team " + player.getDisplayName(), "a"));
						} else {
							if (tm.getTeamLeader(player.getUniqueId()) == player.getUniqueId()) {
								player.sendMessage(u.messageFormat(
										"You are already in a team. Use /team disband to remove your team.", "c"));
							} else {
								player.sendMessage(u.messageFormat(
										"You are already in a team. Use /team leave to leave your team.", "c"));
							}
						}
					}
					if (args[0].equalsIgnoreCase("disband")) {
						if (tm.getTeam(player.getUniqueId()) != null) {
							if (tm.getTeamLeader(player.getUniqueId()) == player.getUniqueId()) {
								tm.deleteTeam(player.getUniqueId());
								player.sendMessage(u.messageFormat("You have disbanded your team.", "a"));
							} else {
								player.sendMessage(u.messageFormat("You are not the team leader.", "c"));
							}
						} else {
							player.sendMessage(u.messageFormat(
									"You are not in a team. Use /team create to generate your team.", "c"));
						}
					}
					if (args[0].equalsIgnoreCase("leave")) {
						if (args.length == 1) {
							if (tm.getTeam(player.getUniqueId()) != null) {
								if (tm.getTeamLeader(player.getUniqueId()) != player.getUniqueId()) {
									Bukkit.getPlayer(tm.getTeamLeader(player.getUniqueId())).sendMessage(
											u.messageFormat(player.getDisplayName() + " has left your team.", "c"));
									player.sendMessage(u.messageFormat("You have left the team.", "a"));
									tm.leaveTeam(tm.getTeamLeader(player.getUniqueId()), player.getUniqueId());
								} else {
									player.sendMessage(u.messageFormat(
											"You are the leader of your team. Use /team disband to remove your team.",
											"c"));
								}
							} else {
								player.sendMessage(u.messageFormat(
										"You are not in a team. Use /team create to generate your team.", "c"));
							}
						} else {
							if (!player.hasPermission("uhc.staff")) {
								player.sendMessage(u.messageFormat("Usage: /team create/disband/leave/info", "c"));
								player.sendMessage(u.messageFormat("Usage: /team invite/join/kick (player)", "c"));
							} else {
								player.sendMessage(u.messageFormat("Usage: /team create/disband/leave/info", "c"));
								player.sendMessage(u.messageFormat("Usage: /team invite/join/kick (player)", "c"));
								player.sendMessage(u.messageFormat("Usage: /team enable/disable", "c"));
							}
						}
					}
					if (args[0].equalsIgnoreCase("info")) {
						if (tm.getTeam(player.getUniqueId()) != null) {
							player.sendMessage(u.messageFormat("--- Team Information ---", "a"));
							player.sendMessage(u.messageFormat(
									"Team " + Bukkit.getPlayer(tm.getTeamLeader(player.getUniqueId())).getDisplayName(),
									"a"));
							ArrayList<String> members = new ArrayList<String>();
							for (UUID playerUUID : tm.getTeamMembers(player.getUniqueId())) {
								members.add(Bukkit.getPlayer(playerUUID).getDisplayName());
							}
							player.sendMessage(u.messageFormat("Members: " + members, "a"));
							player.sendMessage(
									u.messageFormat("Current Size: " + tm.getTeamSize(player.getUniqueId()), "a"));
						} else {
							player.sendMessage(u.messageFormat(
									"You are not in a team. Use /team create to generate your team.", "c"));
						}
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("invite")) {
						if (tm.getTeam(player.getUniqueId()) != null) {
							if (tm.getTeamLeader(player.getUniqueId()) == player.getUniqueId()) {
								if (!args[1].equalsIgnoreCase(player.getDisplayName())) {
									if (Bukkit.getPlayer(args[1]) != null) {
										if (!tm.getInvitedPlayers(player.getUniqueId())
												.contains(Bukkit.getPlayer(args[1]).getUniqueId())) {
											tm.inviteTeamMember(player.getUniqueId(),
													Bukkit.getPlayer(args[1]).getUniqueId());
											player.sendMessage(u.messageFormat(
													"You have invited " + args[1] + " to your team.", "a"));
											Bukkit.getPlayer(args[1])
													.sendMessage(u.messageFormat("You have been invited to "
															+ player.getDisplayName() + "'s team. Use /team join "
															+ player.getDisplayName() + " to join.", "a"));
										} else {
											player.sendMessage(
													u.messageFormat("This player has already been invited.", "c"));
										}
									} else {
										player.sendMessage(u.messageFormat("This player is not online.", "c"));
									}
								} else {
									player.sendMessage(
											u.messageFormat("You cannot invite yourself to your team.", "c"));
								}
							} else {
								player.sendMessage(u.messageFormat("You are not the team leader.", "c"));
							}
						} else {
							player.sendMessage(u.messageFormat(
									"You are not in a team. Use /team create to generate your team.", "c"));
						}
					}
					if (args[0].equalsIgnoreCase("kick")) {
						if (tm.getTeam(player.getUniqueId()) != null) {
							if (tm.getTeamLeader(player.getUniqueId()) == player.getUniqueId()) {
								if (Bukkit.getPlayer(args[1]) != null) {
									if (tm.getTeam(Bukkit.getPlayer(args[1]).getUniqueId()) == TeamManager.getInstance()
											.getTeam(player.getUniqueId())) {
										tm.kickTeamMember(player.getUniqueId(),
												Bukkit.getPlayer(args[1]).getUniqueId());
									} else {
										player.sendMessage(u.messageFormat(
												Bukkit.getPlayer(args[1]).getDisplayName() + " is not in your team.",
												"c"));
									}
								} else {
									@SuppressWarnings("deprecation")
									OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
									if (tm.getTeam(offlinePlayer.getUniqueId()) == TeamManager.getInstance()
											.getTeam(player.getUniqueId())) {
										tm.kickTeamMember(player.getUniqueId(), offlinePlayer.getUniqueId());
									} else {
										player.sendMessage(u
												.messageFormat(offlinePlayer.getName() + " is not in your team.", "c"));
									}
								}
							} else {
								player.sendMessage(u.messageFormat("You are not the team leader.", "c"));
							}
						} else {
							player.sendMessage(u.messageFormat(
									"You are not in a team. Use /team create to generate your team.", "c"));
						}
					}
					if (args[0].equalsIgnoreCase("join")) {
						if (tm.getTeam(player.getUniqueId()) == null) {
							if (tm.getTeam(Bukkit.getPlayer(args[1]).getUniqueId()) != null) {
								if (tm.getInvitedPlayers(Bukkit.getPlayer(args[1]).getUniqueId())
										.contains(player.getUniqueId())) {
									tm.joinTeam(Bukkit.getPlayer(args[1]).getUniqueId(), player.getUniqueId());
									player.sendMessage(u.messageFormat("You have joined " + args[1] + "'s team.", "a"));
									Bukkit.getPlayer(args[1]).sendMessage(
											u.messageFormat(player.getDisplayName() + " has joined your team.", "a"));
								} else {
									player.sendMessage(u.messageFormat(args[1] + " has not invited you yet.", "c"));
								}
							} else {
								player.sendMessage(u.messageFormat(args[1] + " does not have a team.", "c"));
							}
						} else {
							if (tm.getTeamLeader(player.getUniqueId()) == player.getUniqueId()) {
								player.sendMessage(u.messageFormat(
										"You are already in a team. Use /team disband to remove your team.", "c"));
							} else {
								player.sendMessage(u.messageFormat(
										"You are already in a team. Use /team leave to leave your team.", "c"));
							}
						}
					}
				}
			} else if (!tm.areTeamsEnabled() && !player.hasPermission("uhc.staff")) {
				player.sendMessage(u.messageFormat("Teams are currently disabled.", "c"));
			}
		}
		return true;
	}

}