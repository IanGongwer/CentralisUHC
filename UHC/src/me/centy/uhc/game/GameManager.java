package me.centy.uhc.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class GameManager {

	// Singleton of GameManager class
	private static GameManager single_instance = null;

	public static GameManager getInstance() {
		if (single_instance == null) {
			single_instance = new GameManager();
		}
		return single_instance;
	}

	private boolean isPvPEnabled = true;

	public static Map<UUID, Location> predeterminedLocations = new HashMap<UUID, Location>();

	public static Map<UUID, Integer> combatLogTime = new HashMap<UUID, Integer>();

	public static Map<UUID, Integer> playerKills = new HashMap<UUID, Integer>();

	private ArrayList<UUID> spectators = new ArrayList<UUID>();

	private ArrayList<String> activeScenarios = new ArrayList<String>();

	private ArrayList<UUID> players = new ArrayList<UUID>();

	private Location lobbySpawnPoint = new Location(Bukkit.getWorld("world"), -222.5, 52.0, -631.5, 90.0f, 0.0f);
	private Location spectatorSpawnPoint = new Location(Bukkit.getWorld("uhc_world"), 0, 100, 0);

	public Location getLobbySpawnPoint() {
		return lobbySpawnPoint;
	}

	public void setLobbySpawnPoint(String world, double x, double y, double z, float pitch, float yaw) {
		lobbySpawnPoint = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
	}

	public Location getSpectatorSpawnPoint() {
		return spectatorSpawnPoint;
	}

	public void setSpectatorSpawnPoint(String world, double x, double y, double z, float pitch, float yaw) {
		spectatorSpawnPoint = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
	}

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
		if (spectators.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void addPlayer(UUID playerUUID) {
		players.add(playerUUID);
	}

	public void removePlayer(UUID playerUUID) {
		players.remove(playerUUID);
	}

	public void scatterPlayers(ArrayList<UUID> players) {
		GameState.setState(GameState.Scattering);
		PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 10000, 1);
		for (UUID playerUUID : players) {
			Bukkit.getPlayer(playerUUID).getInventory().clear();
			Bukkit.getPlayer(playerUUID).getInventory().setArmorContents(null);
			Bukkit.getPlayer(playerUUID).setFoodLevel(20);
			Bukkit.getPlayer(playerUUID).setHealth(20.0);
			Util.getInstance().createGameScoreboard(Bukkit.getPlayer(playerUUID));
			Bukkit.getPlayer(playerUUID).addPotionEffect(blindness);
			Bukkit.getPlayer(playerUUID).getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
			Location scatterLocation = predeterminedLocations.get(playerUUID);
			scatterLocation.setY(scatterLocation.getY() + 1);
			Bukkit.getPlayer(playerUUID).teleport(scatterLocation);
		}
		for (UUID staff : Util.getInstance().getStaffMode()) {
			Util.getInstance().createStaffSpecScoreboard(Bukkit.getPlayer(staff));
		}
		startGame();
	}

	public Location makeScatterLocation() {
		Random random = new Random();
		int randomX = -501 + random.nextInt(501);
		int randomZ = -501 + random.nextInt(501);
		int yCoord = Bukkit.getWorld("uhc_world").getHighestBlockYAt(randomX, randomZ);
		Location scatterLocation = new Location(Bukkit.getWorld("uhc_world"), randomX, yCoord - 1, randomZ);
		return scatterLocation;
	}

	public Location checkLocationEligibilityNoTeleport(Location potentialScatterLocation) {
		if (potentialScatterLocation.getBlock().getType() != Material.LAVA
				&& potentialScatterLocation.getBlock().getType() != Material.WATER
				&& potentialScatterLocation.getBlock().getType() != Material.STATIONARY_LAVA
				&& potentialScatterLocation.getBlock().getType() != Material.STATIONARY_WATER) {
			return potentialScatterLocation;
		} else {
			return checkLocationEligibilityNoTeleport(makeScatterLocation());
		}
	}

	public void startGame() {
		for (UUID playerUUID : players) {
			Bukkit.getPlayer(playerUUID).removePotionEffect(PotionEffectType.BLINDNESS);
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Util.getInstance().messageFormat("The game has started.", "a"));
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Scenarios: " + getActiveScenarios().toString());
		Bukkit.broadcastMessage(
				Util.getInstance().messageFormat("You can re-log for the first 15 minutes of the game.", "a"));
		Bukkit.broadcastMessage(Util.getInstance().messageFormat("You can kill sheep for string.", "c"));
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("");
		GameState.setState(GameState.InGame);
		Util.getInstance().setWhitelistStatus(false);
	}

	public boolean isPvPEnabled() {
		return isPvPEnabled;
	}

	public void setPvPEnabled(boolean status) {
		isPvPEnabled = status;
	}

	public void isGameFinished() {
		if (getPlayers().size() == 1) {
			GameState.setState(GameState.End);
			UUID winner = getPlayers().get(0);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(
					Util.getInstance().messageFormat("The game has finished. Thank you for playing!", "a"));
			Bukkit.broadcastMessage(Util.getInstance().messageFormat("Discord: discord.centralis.club", "a"));
			Bukkit.broadcastMessage(
					Util.getInstance().messageFormat("Winner: " + Bukkit.getPlayer(winner).getDisplayName(), "a"));
			Bukkit.broadcastMessage("");
		} else {
			return;
		}
	}

	public int getCombatLogTime(UUID playerUUID) {
		return combatLogTime.get(playerUUID);
	}

	public void setCombatLogTime(UUID playerUUID, int time) {
		combatLogTime.put(playerUUID, time);
	}

	public Map<UUID, Integer> getCombatLogMap() {
		return combatLogTime;
	}

	public boolean isCombatLogged(UUID playerUUID) {
		if (combatLogTime.containsKey(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

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

	public int getPlayerKills(UUID playerUUID) {
		return playerKills.get(playerUUID);
	}

	public void setPlayerKills(UUID playerUUID, int kills) {
		playerKills.put(playerUUID, kills);
	}

}