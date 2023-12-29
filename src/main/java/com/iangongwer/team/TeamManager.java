package com.iangongwer.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.utils.Util;

public class TeamManager {

	// Singleton of TeamManager class
	private static TeamManager single_instance = null;

	public static TeamManager getInstance() {
		if (single_instance == null) {
			single_instance = new TeamManager();
		}
		return single_instance;
	}

	private boolean areTeamsEnabled = false;
	private int maxTeamSize = 2;

	private int totalTeams = 0;
	public static Map<UUID, Team> listOfTeams = new HashMap<UUID, Team>();
	public static Map<UUID, Integer> listOfTeamNumbers = new HashMap<UUID, Integer>();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();

	public int getTotalTeams() {
		return totalTeams;
	}

	// Leader Commands
	public void createTeam(UUID leader) {
		Team team = new Team(leader);
		listOfTeams.put(leader, team);
		listOfTeamNumbers.put(leader, totalTeams);
		totalTeams++;
		dbm.createTeam(leader);
	}

	public void deleteTeam(UUID leader) {
		for (UUID member : getTeam(leader).members) {
			if (Bukkit.getPlayer(member) != null) {
				Bukkit.getPlayer(member).sendMessage(Util.getInstance()
						.messageFormat(Bukkit.getPlayer(leader).getDisplayName() + " has disbanded the team", "c"));
				dbm.disbandTeam(leader);
			}
		}

		listOfTeams.remove(leader);
		listOfTeamNumbers.remove(leader);
		totalTeams--;
	}

	public void inviteTeamMember(UUID leader, UUID invited) {
		Team team = listOfTeams.get(leader);
		team.invited.add(invited);
	}

	public void kickTeamMember(UUID leader, UUID member) {
		Team team = listOfTeams.get(leader);
		team.members.remove(member);
		team.size--;
		dbm.removeTeam(member, leader);
	}

	// Player Commands
	public Team getTeam(UUID player) {
		for (Map.Entry<UUID, Team> set : listOfTeams.entrySet()) {
			if (set.getValue().members.contains(player)) {
				return set.getValue();
			}
		}
		return null;
	}

	public boolean hasTeam(UUID player) {
		for (Map.Entry<UUID, Team> set : listOfTeams.entrySet()) {
			if (set.getValue().members.contains(player)) {
				return true;
			}
		}
		return false;
	}

	public int getTeamSize(UUID player) {
		if (getTeam(player) != null) {
			return getTeam(player).size;
		}
		return 0;
	}

	public ArrayList<UUID> getTeamMembers(UUID player) {
		if (getTeam(player) != null) {
			return getTeam(player).members;
		}
		return null;
	}

	public ArrayList<UUID> getDeceasedMembers(UUID player) {
		if (getTeam(player) != null) {
			return getTeam(player).deceased;
		}
		return null;
	}

	public boolean isFullTeamDead(UUID player) {
		if (getTeamMembers(player).size() == getDeceasedMembers(player).size()) {
			listOfTeams.remove(getTeamLeader(player));
			listOfTeamNumbers.remove(getTeamLeader(player));
			totalTeams--;
			return true;
		}
		return false;
	}

	public void addDeceasedMember(UUID player) {
		if (hasTeam(player)) {
			getDeceasedMembers(player).add(player);
		}
	}

	public UUID getTeamLeader(UUID player) {
		if (getTeam(player) != null) {
			return getTeam(player).leader;
		}
		return null;
	}

	public ArrayList<UUID> getInvitedPlayers(UUID leader) {
		if (getTeam(leader) != null) {
			return getTeam(leader).invited;
		}
		return null;
	}

	public void joinTeam(UUID leader, UUID invited) {
		if (getTeam(leader).invited.contains(invited)) {
			getTeam(leader).members.add(invited);
			getTeam(leader).size++;
			dbm.addTeam(invited, leader);
		}
	}

	public void leaveTeam(UUID leader, UUID player) {
		if (getTeam(player) != null) {
			getTeam(player).size--;
			getTeam(player).invited.remove(player);
			getTeam(player).members.remove(player);
			dbm.removeTeam(player, leader);
		}
	}

	public boolean areTeamsEnabled() {
		return areTeamsEnabled;
	}

	public void setTeamsStatus(boolean status) {
		areTeamsEnabled = status;
	}

	public void deleteAllTeams() {
		for (UUID leader : listOfTeams.keySet()) {
			listOfTeams.remove(leader);
			listOfTeamNumbers.remove(leader);
			totalTeams--;
		}
	}

	public void setMaxTeamSize(int teamSize) {
		maxTeamSize = teamSize;
	}

	public int getMaxTeamSize() {
		return maxTeamSize;
	}

}