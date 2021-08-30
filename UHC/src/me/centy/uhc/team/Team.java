package me.centy.uhc.team;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Team {

	private int number;
	private int size;
	private UUID leader;

	private ArrayList<UUID> members = new ArrayList<UUID>();
	private ArrayList<UUID> invitations = new ArrayList<UUID>();

	private Inventory inventory = Bukkit.createInventory(null, 27, "§a§lTEAM BACKPACK");

	public Team(UUID leaderUUID) {
		this.setLeader(leader);
		this.setTeamSize(1);
		this.members.add(leader);
	}

	public int getTeamNumber() {
		return number;
	}

	public void setTeamNumber(int teamNumber) {
		this.number = teamNumber;
	}

	public UUID getLeader() {
		return leader;
	}

	public void setLeader(UUID leader) {
		this.leader = leader;
	}

	public int getTeamSize() {
		return size;
	}

	public void setTeamSize(int size) {
		this.size = size;
	}

	public ArrayList<UUID> getInvitations() {
		return invitations;
	}

	public void sendInvitation(UUID playerUUID) {
		this.invitations.add(playerUUID);
	}

	public ArrayList<UUID> getMembers() {
		return invitations;
	}

	public void addMember(UUID playerUUID) {
		this.members.add(playerUUID);
	}

	public void removeMember(UUID playerUUID) {
		this.members.remove(playerUUID);
	}

	public Inventory getBackPack() {
		return inventory;
	}

	public void setBackPack(Inventory inventory) {
		this.inventory = inventory;
	}

	public boolean isInvited(UUID playerUUID) {
		if (invitations.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMember(UUID playerUUID) {
		if (members.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

}