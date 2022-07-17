package com.iangongwer.commands;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.team.Team;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class CalculateCommand implements CommandExecutor {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("calculate") && sender instanceof Player) {
			Player playerSender = (Player) sender;
			u.setWhitelistStatus(true);
			for (UUID player : gm.getPlayers()) {
				gm.setPlayerKills(player, 0);
				if (TeamManager.getInstance().areTeamsEnabled()) {
					if (!tm.hasTeam(player)) {
						tm.createTeam(player);
					}
				}
			}
			if (TeamManager.getInstance().areTeamsEnabled()) {
				for (Map.Entry<UUID, Team> set : TeamManager.listOfTeams.entrySet()) {
					UUID leader = set.getKey();
					Location scatterLocation = gm.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
					gm.getPredeterminedLocations().put(leader, scatterLocation);
				}
				if (gm.getPredeterminedLocations().size() == TeamManager.getInstance().getTotalTeams()) {
					playerSender.sendMessage(u.messageFormat("All locations calculated.", "a"));
				}
			} else {
				for (UUID player : gm.getPlayers()) {
					Location scatterLocation = gm.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
					gm.getPredeterminedLocations().put(player, scatterLocation);
				}
				if (gm.getPredeterminedLocations().size() == gm.getPlayers().size()) {
					playerSender.sendMessage(u.messageFormat("All locations calculated.", "a"));
				}
			}
		}
		return true;
	}

}