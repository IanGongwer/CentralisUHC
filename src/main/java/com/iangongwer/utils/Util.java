package com.iangongwer.utils;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.team.TeamManager;

public class Util {

	GameManager gm = GameManager.getInstance();

	// Singleton of Util class
	private static Util single_instance = null;

	public static Util getInstance() {
		if (single_instance == null) {
			single_instance = new Util();
		}
		return single_instance;
	}

	private boolean whitelistStatus = true;
	private ArrayList<UUID> whitelistedPlayersUUID = new ArrayList<UUID>();

	private static ArrayList<UUID> staffMode = new ArrayList<UUID>();

	public String messageFormat(String message, String color) {
		return ChatColor.translateAlternateColorCodes('&', '&' + color + message);
	}

	public boolean getWhitelistStatus() {
		return whitelistStatus;
	}

	public void setWhitelistStatus(boolean status) {
		whitelistStatus = status;
	}

	public ArrayList<UUID> getWhitelistedPlayersUUID() {
		return whitelistedPlayersUUID;
	}

	public void addWhitelistedPlayer(UUID playerUUID) {
		whitelistedPlayersUUID.add(playerUUID);
	}

	public void removeWhitelistedPlayer(UUID playerUUID) {
		whitelistedPlayersUUID.remove(playerUUID);
	}

	public void makeSpectator(Player player) {
		UUID playerUUID = player.getUniqueId();

		if (gm.isPlayer(playerUUID)) {
			gm.removePlayer(playerUUID);
			if (TeamManager.getInstance().areTeamsEnabled()) {
				TeamManager.getInstance().addDeceasedMember(playerUUID);
				TeamManager.getInstance().isFullTeamDead(playerUUID);
			}
		} else if (Util.getInstance().isInStaffMode(playerUUID)) {
			Util.getInstance().removeStaffMode(playerUUID);
		}

		player.setFoodLevel(20);
		player.setHealth(20.0);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setGameMode(GameMode.SPECTATOR);
		gm.addSpectator(player.getUniqueId());
		Location loc = new Location(Bukkit.getPlayer(gm.getPlayers().get(0)).getWorld(), 0, 100, 0);
		player.teleport(loc);
	}

	public static ArrayList<UUID> getStaffMode() {
		return staffMode;
	}

	public boolean isInStaffMode(UUID playerUUID) {
		if (staffMode.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public void addStaffMode(UUID playerUUID) {
		staffMode.add(playerUUID);
		gm.removePlayer(playerUUID);
		gm.removeSpectator(playerUUID);
		Player player = Bukkit.getPlayer(playerUUID);
		player.sendMessage(messageFormat("You are now in staff mode.", "a"));
		player.setGameMode(GameMode.SPECTATOR);
		ScoreboardUtil.createStaffSpecScoreboard(Bukkit.getPlayer(playerUUID));
	}

	public void removeStaffMode(UUID playerUUID) {
		staffMode.remove(playerUUID);

		Player player = Bukkit.getPlayer(playerUUID);
		if (GameState.isInGame()) {
			gm.addSpectator(playerUUID);
		}
		if (GameState.isLobby()) {
			gm.addPlayer(playerUUID);
			player.setGameMode(GameMode.SURVIVAL);
		}
		player.sendMessage(messageFormat("You are now not in staff mode.", "c"));
	}
}