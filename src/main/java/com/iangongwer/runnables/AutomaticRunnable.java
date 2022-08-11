package com.iangongwer.runnables;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.commands.ScheduleCommand;
import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.team.Team;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.ChatUtil;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class AutomaticRunnable extends BukkitRunnable {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	static TimeZone timeZone = TimeZone.getTimeZone("UTC");

	@Override
	public void run() {
		if (GameState.isLobby()) {
			Calendar cal = Calendar.getInstance(timeZone);
			cal.set(Calendar.SECOND, 0);
			Calendar cal2 = (Calendar) ScheduleCommand.cal.clone();
			cal2.add(Calendar.MINUTE, -15);
			Calendar cal3 = (Calendar) ScheduleCommand.cal.clone();
			cal3.add(Calendar.MINUTE, 2);
			Calendar cal4 = (Calendar) ScheduleCommand.cal.clone();
			cal4.add(Calendar.MINUTE, 3);
			if (cal.getTime().toString().equals(cal2.getTime().toString())) {
				if (Util.getInstance().getWhitelistStatus()) {
					Util.getInstance().setWhitelistStatus(false);
					Bukkit.broadcastMessage(Util.getInstance().messageFormat("The whitelist is now off", "a"));
				}
			}
			if (cal.getTime().toString().equals(cal3.getTime().toString())) {
				u.setWhitelistStatus(true);
				while (LobbyUtil.getPracticePlayers().size() != 0) {
					for (int i = 0; i < LobbyUtil.getPracticePlayers().size(); i++) {
						LobbyUtil.removePracticePlayer(LobbyUtil.getPracticePlayers().get(i));
						i++;
					}
				}
				for (UUID player : gm.getPlayers()) {
					if (TeamManager.getInstance().areTeamsEnabled()) {
						if (!tm.hasTeam(player)) {
							tm.createTeam(player);
						}
					}
				}
				if (TeamManager.getInstance().areTeamsEnabled()) {
					gm.getPredeterminedLocations().clear();
					for (Map.Entry<UUID, Team> set : TeamManager.listOfTeams.entrySet()) {
						UUID leader = set.getKey();
						Location scatterLocation = gm.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
						gm.getPredeterminedLocations().put(leader, scatterLocation);
					}
				} else {
					gm.getPredeterminedLocations().clear();
					for (UUID player : gm.getPlayers()) {
						Location scatterLocation = gm.checkLocationEligibilityNoTeleport(gm.makeScatterLocation());
						gm.getPredeterminedLocations().put(player, scatterLocation);
					}
				}
			}
			if (cal.getTime().toString().equals(cal4.getTime().toString())) {
				if (gm.getPredeterminedLocations().size() != 0) {
					ChatUtil.setChatMute(true);
					gm.scatterPlayers(gm.getPlayers());
				}
			}
		}
	}

}