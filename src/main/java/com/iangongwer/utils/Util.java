package com.iangongwer.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.runnables.GameRunnable;
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

	private Location practiceSpawnPoint = new Location(Bukkit.getWorld("world"), -211.5, 53.0, -546.5, 90.0f, 0.0f);

	private boolean whitelistStatus = true;
	private ArrayList<UUID> whitelistedPlayersUUID = new ArrayList<UUID>();
	private ArrayList<String> whitelistedPlayersNames = new ArrayList<String>();

	private ArrayList<UUID> practicePlayers = new ArrayList<UUID>();

	private boolean chatMuted = false;
	private ArrayList<UUID> mutedPlayers = new ArrayList<UUID>();

	private ArrayList<UUID> staffMode = new ArrayList<UUID>();

	public boolean isLobby() {
		return GameState.isState(GameState.Lobby);
	}

	public boolean isScattering() {
		return GameState.isState(GameState.Scattering);
	}

	public boolean isInGame() {
		return GameState.isState(GameState.InGame);
	}

	public boolean isEnd() {
		return GameState.isState(GameState.End);
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

	public ArrayList<String> getWhitelistedPlayersNames() {
		return whitelistedPlayersNames;
	}

	public void addWhitelistedPlayer(UUID playerUUID) {
		whitelistedPlayersUUID.add(playerUUID);
		whitelistedPlayersNames.add(Bukkit.getPlayer(playerUUID).getDisplayName());
	}

	public void removeWhitelistedPlayer(UUID playerUUID) {
		whitelistedPlayersUUID.remove(playerUUID);
		whitelistedPlayersNames.remove(Bukkit.getPlayer(playerUUID).getDisplayName());
	}

	public String messageFormat(String message, String color) {
		return ChatColor.translateAlternateColorCodes('&', '&' + color + message);
	}

	public ArrayList<UUID> getPracticePlayers() {
		return practicePlayers;
	}

	public void addPracticePlayer(UUID playerUUID) {
		practicePlayers.add(playerUUID);
		practiceInventory(Bukkit.getPlayer(playerUUID));
	}

	public void removePracticePlayer(UUID playerUUID) {
		practicePlayers.remove(playerUUID);

		Player player = Bukkit.getPlayer(playerUUID);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.teleport(gm.getLobbySpawnPoint());
		player.sendMessage(messageFormat("You have left practice mode. Use /prac to join practice mode.", "c"));
	}

	public boolean isPracticePlayer(UUID playerUUID) {
		if (practicePlayers.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public void practiceInventory(Player player) {
		player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
		player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
		player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
		player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
		player.getInventory().addItem(new ItemStack(Material.BOW));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
		player.teleport(getPracticeSpawnPoint());
		player.sendMessage(messageFormat("You are now in practice mode. Use /prac to leave practice mode.", "a"));
	}

	public ArrayList<UUID> getMutedPlayers() {
		return mutedPlayers;
	}

	public void addMutedPlayer(UUID playerUUID) {
		mutedPlayers.add(playerUUID);
	}

	public void removeMutedPlayer(UUID playerUUID) {
		mutedPlayers.remove(playerUUID);
	}

	public boolean isMuted(UUID playerUUID) {
		if (mutedPlayers.contains(playerUUID)) {
			return true;
		} else {
			return false;
		}
	}

	public String getGroupPrefix(Player player) {
		if (isPlayerInGroup(player, "OWNER")) {
			return ChatColor.RED + "OWNER " + ChatColor.WHITE;
		}
		if (isPlayerInGroup(player, "STAFF")) {
			return ChatColor.BLUE + "STAFF " + ChatColor.WHITE;
		}
		if (isPlayerInGroup(player, "TWITCH")) {
			return ChatColor.LIGHT_PURPLE + "TWITCH " + ChatColor.WHITE;
		}
		if (isPlayerInGroup(player, "YOUTUBE")) {
			return ChatColor.YELLOW + "YOUTUBE " + ChatColor.WHITE;
		}
		if (GameManager.getInstance().isSpectator(player.getUniqueId())) {
			return ChatColor.GRAY + "SPEC " + ChatColor.WHITE;
		}
		return ChatColor.WHITE + "";

	}

	public boolean isPlayerInGroup(Player player, String group) {
		return player.hasPermission("group." + group.toUpperCase());
	}

	public String chatMessage(String message, Player player) {
		if (TeamManager.getInstance().getTeam(player.getUniqueId()) != null) {
			return getGroupPrefix(player) + ChatColor.GOLD + "["
					+ Bukkit.getPlayer(TeamManager.getInstance().getTeamLeader(player.getUniqueId())).getDisplayName()
							.toUpperCase()
					+ "] " + ChatColor.WHITE + player.getDisplayName() + ": " + message;
		}
		return getGroupPrefix(player) + player.getDisplayName() + ": " + message;
	}

	public boolean isChatMuted() {
		return chatMuted;
	}

	public void setChatMute(boolean mute) {
		chatMuted = mute;
	}

	public void createWorld(String worldName) {
		if (Bukkit.getWorld(worldName) == null) {
			File deleteFilePath = new File("C:\\Users\\Ian\\Documents\\CentralisClub\\Server\\" + worldName);
			deleteWorld(deleteFilePath);
			WorldCreator worldcreate = new WorldCreator(worldName);
			worldcreate.environment(World.Environment.NORMAL);
			worldcreate.type(WorldType.NORMAL);
			worldcreate.createWorld();
			// Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + " set
			// 500 500 0 0");
			// Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + worldName + "
			// fill");
			// Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
			clearZeroZero();
			Bukkit.getLogger().info("Loading uhc_world...");
		}
	}

	public void clearZeroZero() {
		for (int x = -100; x <= 100; x++) {
			for (int y = 40; y <= 100; y++) {
				for (int z = -100; z <= 100; z++) {
					Block block = Bukkit.getServer().getWorld("uhc_world").getBlockAt(x, y, z);
					if (block.getType() == Material.LOG || block.getType() == Material.LOG_2
							|| block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
							|| block.getType() == Material.HUGE_MUSHROOM_1
							|| block.getType() == Material.HUGE_MUSHROOM_2) {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}

	public boolean deleteWorld(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public void createLobbyScoreboard(Player player) {
		BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
		board.set("Waiting for players...", 15);
	}

	public void createGameScoreboard(Player player) {
		if (hasScoreboard(player)) {
			BPlayerBoard board = getScoreboard(player);
			board.set(ChatColor.BOLD + "Time", 15);
			board.set(GameRunnable.getFormattedTime(), 14);
			board.set(ChatColor.BOLD + "Kills", 13);
			int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
			board.set(String.valueOf(playerKills), 12);
		} else {
			BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
			board.set(ChatColor.BOLD + "Time", 15);
			board.set(GameRunnable.getFormattedTime(), 14);
			board.set(ChatColor.BOLD + "Kills", 13);
			int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
			board.set(String.valueOf(playerKills), 12);
		}
	}

	public void createStaffSpecScoreboard(Player player) {
		if (hasScoreboard(player)) {
			BPlayerBoard board = getScoreboard(player);
			board.set(ChatColor.BOLD + "Time", 15);
			board.set(GameRunnable.getFormattedTime(), 14);
		} else {
			BPlayerBoard board = Netherboard.instance().createBoard(player, "uhc.centralis.cc");
			board.set(ChatColor.BOLD + "Time", 15);
			board.set(GameRunnable.getFormattedTime(), 14);
		}
	}

	public boolean hasScoreboard(Player player) {
		BPlayerBoard board = Netherboard.instance().getBoard(player);
		if (board == null) {
			return false;
		} else {
			return true;
		}
	}

	public BPlayerBoard getScoreboard(Player player) {
		BPlayerBoard board = Netherboard.instance().getBoard(player);
		return board;
	}

	public void updateTime(Player player) {
		BPlayerBoard board = getScoreboard(player);
		board.set(GameRunnable.getFormattedTime(), 14);
	}

	public void updateKills(Player player) {
		BPlayerBoard board = getScoreboard(player);
		int playerKills = GameManager.getInstance().getPlayerKills(player.getUniqueId());
		board.set(String.valueOf(playerKills), 12);
	}

	public void spawnFireworks(Location location, int amount) {
		Location loc = location;
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		fwm.setPower(2);
		fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).build());

		fw.setFireworkMeta(fwm);
		fw.detonate();

		for (int i = 0; i < amount; i++) {
			Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			fw2.setFireworkMeta(fwm);
		}
	}

	public void joinLobbyUtil(Player player) {
		for (Player allPlayers : Bukkit.getOnlinePlayers()) {
			allPlayers.showPlayer(player);
			player.showPlayer(allPlayers);
		}
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setFoodLevel(20);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.sendMessage("");
		player.sendMessage("Welcome to Centralis UHC. Use /prac to join the practice arena.");
		player.sendMessage("");
		player.teleport(gm.getLobbySpawnPoint());
		player.getActivePotionEffects().clear();
		player.setExp(0);
		player.setGameMode(GameMode.SURVIVAL);
		gm.addPlayer(player.getUniqueId());
		createLobbyScoreboard(player);
	}

	public void makeSpectator(Player player) {
		player.setFoodLevel(20);
		player.setHealth(20.0);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		for (Player allPlayers : Bukkit.getOnlinePlayers()) {
			allPlayers.hidePlayer(player);
		}
		player.setGameMode(GameMode.SPECTATOR);
	}

	public ArrayList<UUID> getStaffMode() {
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
		for (Player allPlayers : Bukkit.getOnlinePlayers()) {
			allPlayers.hidePlayer(player);
		}
		player.sendMessage(messageFormat("You are now in staff mode.", "a"));
		player.setGameMode(GameMode.SPECTATOR);
		Util.getInstance().createStaffSpecScoreboard(Bukkit.getPlayer(playerUUID));
	}

	public void removeStaffMode(UUID playerUUID) {
		staffMode.remove(playerUUID);

		Player player = Bukkit.getPlayer(playerUUID);
		if (isInGame()) {
			gm.addSpectator(playerUUID);
		}
		if (isLobby()) {
			gm.addPlayer(playerUUID);
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				allPlayers.showPlayer(player);
			}
			player.setGameMode(GameMode.SURVIVAL);
		}
		player.sendMessage(messageFormat("You are now not in staff mode.", "c"));
	}

	public Location getPracticeSpawnPoint() {
		return practiceSpawnPoint;
	}

	public void setPracticeSpawnPoint(String world, double x, double y, double z, float pitch, float yaw) {
		practiceSpawnPoint = new Location(Bukkit.getWorld(world), x, y, z, pitch, yaw);
	}

	public void spawnVillager(Player player) {
		Villager quitLoggedVillager = (Villager) player.getWorld().spawnEntity(player.getLocation(),
				EntityType.VILLAGER);
		quitLoggedVillager.setCustomName(player.getDisplayName());
		quitLoggedVillager.setCustomNameVisible(true);
	}

	public void despawnVillager(OfflinePlayer player) {
		for (LivingEntity entity : Bukkit.getWorld("uhc_world").getLivingEntities()) {
			if (entity instanceof Villager && entity.getCustomName() != null) {
				if (entity.getCustomName()
						.equalsIgnoreCase(player.getName())) {
					entity.damage(20);
					gm.setQuitLogTime(player.getUniqueId(), -1);
					gm.removeQuitLoggedPlayer(player.getUniqueId());
				}
			}
		}
	}

	Random random = new Random();

	public void spawnSupplyDrop(int x, int y, int z) {
		Block supplyDrop = Bukkit.getWorld("uhc_world").getBlockAt(x, y, z);
		supplyDrop.setType(Material.CHEST);

		Chest chest = (Chest) supplyDrop.getState();
		ArrayList<Material> possibleItems = new ArrayList<Material>(
				Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS,
						Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.GOLDEN_APPLE, Material.DIAMOND,
						Material.GOLD_INGOT, Material.ARROW, Material.STRING, Material.ENCHANTMENT_TABLE));

		for (int i = 0; i < 4; i++) {
			int item = random.nextInt(11);
			if (possibleItems.get(item) == Material.ARROW) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 32));
			}
			if (possibleItems.get(item) == Material.GOLDEN_APPLE) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 3));
			}
			if (possibleItems.get(item) == Material.DIAMOND) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 2));
			}
			if (possibleItems.get(item) == Material.STRING) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 3));
			}
			if (possibleItems.get(item) == Material.GOLD_INGOT) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 9));
			}
			if (possibleItems.get(item) != Material.ARROW && possibleItems.get(item) != Material.GOLDEN_APPLE
					&& possibleItems.get(item) != Material.DIAMOND && possibleItems.get(item) != Material.STRING
					&& possibleItems.get(item) != Material.GOLD_INGOT) {
				chest.getInventory().addItem(new ItemStack(possibleItems.get(item), 1));
			}
		}
	}
}