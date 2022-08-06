package com.iangongwer.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.team.Team;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.HeartUtil;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;

public class GameManager {

	// Singleton of GameManager class
	private static GameManager single_instance = null;

	public static GameManager getInstance() {
		if (single_instance == null) {
			single_instance = new GameManager();
		}
		return single_instance;
	}

	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	Random random = new Random();

	private boolean isPvPEnabled = false;

	private Map<UUID, Location> predeterminedLocations = new HashMap<UUID, Location>();

	public static Map<UUID, ArrayList<ItemStack>> deathInventories = new HashMap<UUID, ArrayList<ItemStack>>();
	private Map<UUID, Location> deathLocations = new HashMap<UUID, Location>();

	public static Map<UUID, Integer> quitLogTime = new HashMap<UUID, Integer>();
	public static Map<UUID, ArrayList<ItemStack>> quitLoggedInventories = new HashMap<UUID, ArrayList<ItemStack>>();
	private ArrayList<UUID> quitLoggedPlayers = new ArrayList<UUID>();

	public static Map<UUID, Integer> playerKills = new HashMap<UUID, Integer>();

	private ArrayList<UUID> spectators = new ArrayList<UUID>();

	private ArrayList<String> activeScenarios = new ArrayList<String>();

	private ArrayList<UUID> players = new ArrayList<UUID>();
	private ArrayList<UUID> alreadyScattered = new ArrayList<UUID>();

	PotionEffect damageResistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 10);

	public ArrayList<UUID> getSpectators() {
		return spectators;
	}

	public void addSpectator(UUID playerUUID) {
		spectators.add(playerUUID);
	}

	public void removeSpectator(UUID playerUUID) {
		spectators.remove(playerUUID);
	}

	public boolean isSpectator(UUID playerUUID) {
		return spectators.contains(playerUUID);
	}

	// Player Handling
	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void addPlayer(UUID playerUUID) {
		players.add(playerUUID);
	}

	public void removePlayer(UUID playerUUID) {
		players.remove(playerUUID);
	}

	public boolean isPlayer(UUID playerUUID) {
		return getPlayers().contains(playerUUID);
	}

	// Scattering
	public ArrayList<UUID> getAlreadyScatteredPlayers() {
		return alreadyScattered;
	}

	public void addScatteredPlayer(UUID playerUUID) {
		alreadyScattered.add(playerUUID);
	}

	public void removeScatteredPlayer(UUID playerUUID) {
		alreadyScattered.remove(playerUUID);
	}

	public void playerScatterUtil(Player player) {
		if (GameManager.getInstance().isScenarioActive("Fireless")) {
			PotionEffect fire_resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 1);
			player.addPotionEffect(fire_resistance);
		}

		player.addPotionEffect(damageResistance);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setFoodLevel(20);
		player.setMaxHealth(24.0);
		player.setHealth(20.0);
		player.setExp(0f);
		player.setLevel(0);
		player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 32));
		player.getInventory().addItem(new ItemStack(Material.LEATHER, 1));
		player.getInventory().addItem(new ItemStack(Material.SUGAR_CANE, 3));
		ScoreboardUtil.createGameScoreboard(player);
		HeartUtil.showHealth(player, ScoreboardUtil.getScoreboard(player).getScoreboard(),
				ScoreboardUtil.getScoreboard(player).getName());
	}

	public void scatterPlayers(ArrayList<UUID> players) {
		GameState.setState(GameState.Scattering);
		TeamManager.getInstance();

		if (TeamManager.getInstance().areTeamsEnabled()) {
			for (Map.Entry<UUID, Team> set : TeamManager.listOfTeams.entrySet()) {
				UUID leader = set.getKey();

				addScatteredPlayer(leader);
				Bukkit.getPlayer(leader).teleport(getPredeterminedLocations().get(leader));
				playerScatterUtil(Bukkit.getPlayer(leader));
			}

			for (UUID player : players) {
				if (!getAlreadyScatteredPlayers().contains(player)) {
					UUID leader = TeamManager.getInstance().getTeamLeader(player);
					Location leaderLoc = Bukkit.getPlayer(leader).getLocation();

					addScatteredPlayer(player);
					leaderLoc.setY(leaderLoc.getY() + 1);
					Bukkit.getPlayer(player).teleport(leaderLoc);

					playerScatterUtil(Bukkit.getPlayer(player));
				}
			}
		} else {
			for (UUID player : getPlayers()) {
				addScatteredPlayer(player);
				Bukkit.getPlayer(player).teleport(getPredeterminedLocations().get(player));
				playerScatterUtil(Bukkit.getPlayer(player));
			}
		}

		GameRunnable.setBorderBlock(1500);
		startGame();
	}

	public Location makeScatterLocation() {
		int randomX = -751 + random.nextInt(751);
		int randomZ = -751 + random.nextInt(751);
		int yCoord = Bukkit.getWorld("uhc_world").getHighestBlockYAt(randomX, randomZ) + 100;

		if (yCoord < 60) {
			makeScatterLocation();
		}

		Location scatterLocation = new Location(Bukkit.getWorld("uhc_world"), randomX, yCoord, randomZ);
		return scatterLocation;
	}

	public Location checkLocationEligibilityNoTeleport(Location potentialScatterLocation) {
		if (potentialScatterLocation.getBlock().getType() != Material.LAVA
				&& potentialScatterLocation.getBlock().getType() != Material.WATER
				&& potentialScatterLocation.getBlock().getType() != Material.STATIONARY_LAVA
				&& potentialScatterLocation.getBlock().getType() != Material.STATIONARY_WATER
				&& potentialScatterLocation.getBlock().getType() != Material.STONE) {
			return potentialScatterLocation;
		} else {
			return checkLocationEligibilityNoTeleport(makeScatterLocation());
		}
	}

	// PvP Enabling
	public boolean isPvPEnabled() {
		return isPvPEnabled;
	}

	public void setPvPEnabled(boolean status) {
		isPvPEnabled = status;
	}

	// Start and End Games
	public void startGame() {
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Util.getInstance().messageFormat("[UHC] The game has started.", "a"));
		Bukkit.broadcastMessage(Util.getInstance().messageFormat(
				"[UHC] You can relog till PvP enables (15 mins). If you relog after PvP enables, you will become a spectator ",
				"a"));
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Util.getInstance()
				.messageFormat("[UHC] " + ChatColor.YELLOW + "Scenarios: " + getActiveScenarios().toString(), "a"));
		Bukkit.broadcastMessage(
				Util.getInstance().messageFormat("[UHC] Gather 3 wool and craft it to make string", "a"));
		Bukkit.broadcastMessage(Util.getInstance().messageFormat(
				"[UHC] Create a novice sword and string from wool this game using this recipe: http://centralis.cc/recipes.html",
				"a"));
		Bukkit.broadcastMessage("");

		GameState.setState(GameState.InGame);
		Util.getInstance().setWhitelistStatus(false);
	}

	public void isGameFinished(UUID killer) {
		if (TeamManager.getInstance().areTeamsEnabled()) {
			if (TeamManager.getInstance().getTotalTeams() == 1) {
				GameState.setState(GameState.End);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(
						Util.getInstance().messageFormat("The game has finished. Thank you for playing!", "a"));
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Discord: discord.centralis.cc", "a"));
				ArrayList<String> members = new ArrayList<String>();
				for (UUID playerUUID : TeamManager.getInstance().getTeamMembers(killer)) {
					members.add(Bukkit.getPlayer(playerUUID).getDisplayName());

					if (!Main.isRedisEnabled()) {
						dbm.addWin(playerUUID);
					} else {
						cr.addWin(playerUUID);
					}

				}
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Winners: " + members, "a"));
				Bukkit.broadcastMessage("");
			}
		} else {
			if (getPlayers().size() == 1) {
				GameState.setState(GameState.End);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(
						Util.getInstance().messageFormat("The game has finished. Thank you for playing!", "a"));
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Discord: discord.centralis.cc", "a"));
				Bukkit.broadcastMessage(
						Util.getInstance().messageFormat("Winner: " + Bukkit.getPlayer(killer).getDisplayName(), "a"));
				Bukkit.broadcastMessage("");

				if (!Main.isRedisEnabled()) {
					dbm.addWin(Bukkit.getPlayer(killer).getUniqueId());
				} else {
					cr.addWin(Bukkit.getPlayer(killer).getUniqueId());
				}
			}
		}
	}

	public void isGameFinishedVillager() {
		if (TeamManager.getInstance().areTeamsEnabled()) {
			if (TeamManager.getInstance().getTotalTeams() == 1) {
				GameState.setState(GameState.End);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(
						Util.getInstance().messageFormat("The game has finished. Thank you for playing!", "a"));
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Discord: discord.centralis.cc", "a"));
				ArrayList<String> members = new ArrayList<String>();
				for (UUID playerUUID : TeamManager.getInstance().getTeamMembers(getPlayers().get(0))) {
					members.add(Bukkit.getPlayer(playerUUID).getDisplayName());

					if (!Main.isRedisEnabled()) {
						dbm.addWin(playerUUID);
					} else {
						cr.addWin(playerUUID);
					}
				}
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Winners: " + members, "a"));
				Bukkit.broadcastMessage("");
			}
		} else {
			if (getPlayers().size() == 1) {
				GameState.setState(GameState.End);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(
						Util.getInstance().messageFormat("The game has finished. Thank you for playing!", "a"));
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Discord: discord.centralis.cc", "a"));
				Bukkit.broadcastMessage(Util.getInstance()
						.messageFormat("Winner: " + Bukkit.getPlayer(getPlayers().get(0)).getDisplayName(), "a"));
				Bukkit.broadcastMessage("");

				if (!Main.isRedisEnabled()) {
					dbm.addWin(Bukkit.getPlayer(getPlayers().get(0)).getUniqueId());
				} else {
					cr.addWin(Bukkit.getPlayer(getPlayers().get(0)).getUniqueId());
				}
			}
		}
	}

	// Quit Log Time
	public int getQuitLogTime(UUID playerUUID) {
		return quitLogTime.get(playerUUID);
	}

	public void setQuitLogTime(UUID playerUUID, int time) {
		quitLogTime.put(playerUUID, time);
	}

	public Map<UUID, Integer> getQuitLogMap() {
		return quitLogTime;
	}

	public boolean isQuitLogged(UUID playerUUID) {
		if (quitLogTime.containsKey(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public void addQuitLoggedPlayer(UUID playerUUID) {
		quitLoggedPlayers.add(playerUUID);
	}

	public void removeQuitLoggedPlayer(UUID playerUUID) {
		quitLoggedPlayers.remove(playerUUID);
	}

	public ArrayList<UUID> getQuitLoggedPlayers() {
		return quitLoggedPlayers;
	}

	public void storeQuitLoggedInventories(UUID playerUUID) {
		ArrayList<ItemStack> inventoryMakeup = new ArrayList<ItemStack>();
		for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				inventoryMakeup.add(item);
			}
		}
		for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) {
				inventoryMakeup.add(item);
			}
		}
		quitLoggedInventories.put(playerUUID, inventoryMakeup);
	}

	public ArrayList<ItemStack> getQuitLoggedInventory(UUID playerUUID) {
		return quitLoggedInventories.get(playerUUID);
	}

	// Scenarios
	public ArrayList<String> getActiveScenarios() {
		return activeScenarios;
	}

	public void enableScenario(String scenario) {
		activeScenarios.add(scenario.toUpperCase());
	}

	public void disableScenario(String scenario) {
		activeScenarios.remove(scenario.toUpperCase());
	}

	public boolean isScenarioActive(String scenario) {
		if (getActiveScenarios().contains(scenario.toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}

	// Kills
	public int getPlayerKills(UUID playerUUID) {
		return playerKills.get(playerUUID);
	}

	public void setPlayerKills(UUID playerUUID, int kills) {
		playerKills.put(playerUUID, kills);
	}

	public Map<UUID, Location> getPredeterminedLocations() {
		return predeterminedLocations;
	}

	// Death Inventories
	public void storeDeathInventories(UUID playerUUID, List<ItemStack> inventory) {
		ArrayList<ItemStack> inventoryMakeup = new ArrayList<ItemStack>();
		for (ItemStack item : inventory) {
			if (item != null && item.getType() != Material.AIR) {
				inventoryMakeup.add(item);
			}
		}
		for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR) {
				inventoryMakeup.add(item);
			}
		}
		deathInventories.put(playerUUID, inventoryMakeup);
	}

	public ArrayList<ItemStack> getDeathInventory(UUID playerUUID) {
		return deathInventories.get(playerUUID);
	}

	// Death Locations
	public Map<UUID, Location> getDeathLocations() {
		return deathLocations;
	}

}