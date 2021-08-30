package me.centy.uhc.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

	// Singleton of GameManager class
	private static TeamManager single_instance = null;

	public static TeamManager getInstance() {
		if (single_instance == null) {
			single_instance = new TeamManager();
		}
		return single_instance;
	}

	private Map<Integer, Team> identifierToInstance = new HashMap<>();

	public Map<UUID, Integer> teamNumberOfPlayer = new HashMap<>();

	private int totalTeamCount = 0;

	private int teamSizeCap = 2;

	public void createTeam(UUID playerUUID) {
		Team team = new Team(playerUUID);
		totalTeamCount++;
		team.setTeamNumber(totalTeamCount);
		identifierToInstance.put(totalTeamCount, team);
	}

	public void disbandTeam(int teamNumber) {
		ArrayList<UUID> members = getTeamInstance(teamNumber).getMembers();
		for (UUID member : members) {
			teamNumberOfPlayer.remove(member);
		}
		identifierToInstance.remove(teamNumber);
	}

	public void sendInvitation(UUID leader, UUID playerUUID) {
		getTeamInstance(getTeamOfPlayer(leader)).sendInvitation(playerUUID);
	}

	public void acceptInvitation(UUID leader, UUID playerUUID) {
		if (getTeamInstance(getTeamOfPlayer(leader)).isInvited(playerUUID)) {
			if (getTeamInstance(getTeamOfPlayer(leader)).getTeamSize() <= teamSizeCap) {
				getTeamInstance(getTeamOfPlayer(leader)).addMember(playerUUID);
				getTeamInstance(getTeamOfPlayer(leader))
						.setTeamSize(getTeamInstance(getTeamOfPlayer(leader)).getTeamSize() + 1);
			}
		}
	}

	public int getTeamOfPlayer(UUID playerUUID) {
		return teamNumberOfPlayer.get(playerUUID);
	}

	public Team getTeamInstance(int teamNumber) {
		Team team = identifierToInstance.get(teamNumber);
		return team;
	}

	public int getTotalTeamCount() {
		return totalTeamCount;
	}

	public int getTeamSizeCap() {
		return teamSizeCap;
	}

	public void setTeamSizeCap(int teamSizeCap) {
		this.teamSizeCap = teamSizeCap;
	}

}