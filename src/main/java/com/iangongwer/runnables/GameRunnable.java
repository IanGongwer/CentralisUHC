package com.iangongwer.runnables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.scenarios.SupplyDrops;
import com.iangongwer.scenarios.TimeBomb;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;

public class GameRunnable extends BukkitRunnable {

	static Util u = Util.getInstance();
	static GameManager gm = GameManager.getInstance();
	static TimeBomb tb = TimeBomb.getInstance();

	static Random random = new Random();
	private static int totalTime = 0;
	static ArrayList<Map.Entry<Location, Integer>> listOfSets = new ArrayList<Map.Entry<Location, Integer>>();

	@Override
	public void run() {
		if (GameState.isInGame()) {
			totalTime++;

			if (gm.isScenarioActive("TimeBomb")) {
				for (Map.Entry<Location, Integer> set : TimeBomb.timeBombTime.entrySet()) {
					subtractTimeBombTime(set);
				}
				blowUpChest();
			}

			spectatorAreaProcedure();
			updatePlayerScoreboard();
			announcementBeforeFinalHealMessage();
			announcementDiscordMessage();
			announcementFinalHealMessage();
			supplyDropCheck();
			announcementPvPEnabledMessage();
			borderShrinkMessage();
			borderShrinkWarningMessage();
		}
	}

	public static String getFormattedTime() {
		int second = totalTime % 60;
		int minute = totalTime / 60;
		if (minute >= 60) {
			int hour = minute / 60;
			minute %= 60;
			return hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
		}
		return minute + ":" + (second < 10 ? "0" + second : second);
	}

	public static int getSecondsPassed() {
		return totalTime;
	}

	private static void spectatorAreaProcedure() {
		for (UUID spectator : gm.getSpectators()) {
			if (Bukkit.getPlayer(spectator) != null) {
				if (Bukkit.getPlayer(spectator).getLocation().getBlockX() > 50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockX() < -50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockZ() > 50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockZ() < -50) {
					for (UUID playerUUID : gm.getPlayers()) {
						if (Bukkit.getPlayer(playerUUID) != null) {
							Location loc = new Location(Bukkit.getPlayer(playerUUID).getWorld(), 0, 100, 0);
							Bukkit.getPlayer(spectator).teleport(loc);
						}
					}
				}
			}
		}
	}

	private static void updatePlayerScoreboard() {
		for (Player allPlayers : Bukkit.getOnlinePlayers()) {
			if (ScoreboardUtil.hasScoreboard(allPlayers)) {
				ScoreboardUtil.updateGameTime(allPlayers);
			}
		}
	}

	private static void subtractTimeBombTime(Map.Entry<Location, Integer> set) {
		if (set.getValue() >= 1) {
			set.setValue(set.getValue() - 1);
		} else if (set.getValue() == 0) {
			listOfSets.add(set);
		}
	}

	private static void blowUpChest() {
		for (Entry<Location, Integer> set2 : listOfSets) {
			if (TimeBomb.timeBombTime.containsKey(set2.getKey())) {
				Bukkit.getWorld("uhc_world").createExplosion(set2.getKey(), 4F, true);
				TimeBomb.removeTimeBombTime(set2.getKey());
			}
		}
	}

	private static void announcementBeforeFinalHealMessage() {
		if (getFormattedTime().equalsIgnoreCase("5:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Final heal is in 5 minutes!", "a"));
			Bukkit.broadcastMessage("");
			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) != null) {
					Bukkit.getWorld("uhc_world").playSound(Bukkit.getPlayer(playerUUID).getLocation(),
							Sound.FIREWORK_LAUNCH, 3.0F, 0.533F);
				}
			}
		}
	}

	private static void announcementFinalHealMessage() {
		if (getFormattedTime().equalsIgnoreCase("10:00")) {
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				allPlayers.setHealth(20.0);
			}

			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] All players are now healed!", "a"));
			Bukkit.broadcastMessage("");

			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) != null) {
					Bukkit.getWorld("uhc_world").playSound(Bukkit.getPlayer(playerUUID).getLocation(),
							Sound.FIREWORK_LAUNCH, 3.0F, 0.533F);
				}
			}
		}
	}

	private static void announcementPvPEnabledMessage() {
		if (getFormattedTime().equalsIgnoreCase("15:00")) {
			gm.setPvPEnabled(true);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] PvP is now enabled!", "a"));
			Bukkit.broadcastMessage("");

			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) != null) {
					Bukkit.getWorld("uhc_world").playSound(Bukkit.getPlayer(playerUUID).getLocation(),
							Sound.FIREWORK_LAUNCH, 3.0F, 0.533F);
				}
			}
		}
	}

	private static void announcementDiscordMessage() {
		if (getFormattedTime().equalsIgnoreCase("7:00") || getFormattedTime().equalsIgnoreCase("17:00")
				|| getFormattedTime().equalsIgnoreCase("27:00") || getFormattedTime().equalsIgnoreCase("37:00")
				|| getFormattedTime().equalsIgnoreCase("47:00")) {
			Bukkit.broadcastMessage(u.messageFormat(
					"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
		}
	}

	private static void borderShrinkMessage() {
		if (getFormattedTime().equalsIgnoreCase("30:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 500 500 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 500x500!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
			setBorderBlock(500);
		}
		if (getFormattedTime().equalsIgnoreCase("35:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 250 250 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 250x250!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
			setBorderBlock(250);
		}
		if (getFormattedTime().equalsIgnoreCase("40:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 100 100 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 100x100!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
			setBorderBlock(100);
		}
		if (getFormattedTime().equalsIgnoreCase("45:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 50 50 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 50x50!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
			setBorderBlock(50);
		}
		if (getFormattedTime().equalsIgnoreCase("50:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 25 25 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 25x25!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
			setBorderBlock(25);
			teleportPlayersUp();
		}
	}

	private static void borderShrinkWarningMessage() {
		if (getFormattedTime().equalsIgnoreCase("29:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 500x500 in 1 minute", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("34:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 250x250 in 1 minute", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("39:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 100x100 in 1 minute", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("44:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 50x50 in 1 minute", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("49:00")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 25x25 in 1 minute", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
	}

	private static void supplyDropCheck() {
		if (gm.isScenarioActive("SupplyDrops")) {
			if (getFormattedTime().equalsIgnoreCase("10:00") || getFormattedTime().equalsIgnoreCase("15:00")) {
				int x = -2000 + random.nextInt(2000);
				int z = -2000 + random.nextInt(2000);
				int y = 90;
				SupplyDrops.spawnSupplyDrop(x, y, z);
				Bukkit.broadcastMessage(u.messageFormat(
						"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
			} else if (getFormattedTime().equalsIgnoreCase("30:00")) {
				int x = -501 + random.nextInt(501);
				int z = -501 + random.nextInt(501);
				int y = 90;
				SupplyDrops.spawnSupplyDrop(x, y, z);
				Bukkit.broadcastMessage(u.messageFormat(
						"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
			} else if (getFormattedTime().equalsIgnoreCase("35:00")) {
				int x = -251 + random.nextInt(251);
				int z = -251 + random.nextInt(251);
				int y = 90;
				SupplyDrops.spawnSupplyDrop(x, y, z);
				Bukkit.broadcastMessage(u.messageFormat(
						"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));

			} else if (getFormattedTime().equalsIgnoreCase("40:00")) {
				int x = -101 + random.nextInt(101);
				int z = -101 + random.nextInt(101);
				int y = 90;
				SupplyDrops.spawnSupplyDrop(x, y, z);
				Bukkit.broadcastMessage(u.messageFormat(
						"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
			}
		}
	}

	public static void playMessageSound() {
		for (UUID playerUUID : gm.getPlayers()) {
			if (Bukkit.getPlayer(playerUUID) != null) {
				Bukkit.getWorld("uhc_world").playSound(Bukkit.getPlayer(playerUUID).getLocation(),
						Sound.FIREWORK_LAUNCH, 3.0F, 0.533F);
			}
		}
	}

	public static void setBorderBlock(int borderDiameter) {
		int borderRadius = borderDiameter / 2;
		for (int num = borderRadius; num >= -borderRadius; num--) {
			Bukkit.getWorld("uhc_world")
					.getBlockAt(num, Bukkit.getWorld("uhc_world").getHighestBlockYAt(num, borderRadius) - 1,
							borderRadius)
					.setType(Material.BEDROCK);
		}
		for (int num = borderRadius; num > -borderRadius; num--) {
			Bukkit.getWorld("uhc_world")
					.getBlockAt(borderRadius, Bukkit.getWorld("uhc_world").getHighestBlockYAt(borderRadius, num) - 1,
							num)
					.setType(Material.BEDROCK);
			;
		}
		for (int num = borderRadius; num >= -borderRadius; num--) {
			Bukkit.getWorld("uhc_world")
					.getBlockAt(-num, Bukkit.getWorld("uhc_world").getHighestBlockYAt(-num, -borderRadius) - 1,
							-borderRadius)
					.setType(Material.BEDROCK);
		}
		for (int num = borderRadius; num > -borderRadius; num--) {
			Bukkit.getWorld("uhc_world")
					.getBlockAt(-borderRadius, Bukkit.getWorld("uhc_world").getHighestBlockYAt(-borderRadius, -num) - 1,
							-num)
					.setType(Material.BEDROCK);
		}
	}

	private static void teleportPlayersUp() {
		for (UUID playerUUID : gm.getPlayers()) {
			Location newLocation = new Location(Bukkit.getPlayer(playerUUID).getWorld(),
					Bukkit.getPlayer(playerUUID).getLocation().getBlockX(),
					Bukkit.getPlayer(playerUUID).getWorld().getHighestBlockYAt(
							Bukkit.getPlayer(playerUUID).getLocation().getBlockX(),
							Bukkit.getPlayer(playerUUID).getLocation().getBlockZ()) + 3,
					Bukkit.getPlayer(playerUUID).getLocation().getBlockZ());
			Bukkit.getPlayer(playerUUID).teleport(newLocation);
		}
	}

}