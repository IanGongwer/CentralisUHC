package com.iangongwer.runnables;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TimeBomb tb = TimeBomb.getInstance();

	Random random = new Random();
	private static int totalTime = 0;

	@Override
	public void run() {
		if (GameState.isInGame()) {
			totalTime++;

			if (gm.isScenarioActive("TimeBomb")) {
				for (Map.Entry<Location, Integer> set : TimeBomb.timeBombTime.entrySet()) {
					subtractTimeBombTime(set);
					blowUpChest(set);
				}
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

	private void spectatorAreaProcedure() {
		for (UUID spectator : gm.getSpectators()) {
			if (Bukkit.getPlayer(spectator) != null) {
				if (Bukkit.getPlayer(spectator).getLocation().getBlockX() > 50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockX() < -50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockZ() > 50
						|| Bukkit.getPlayer(spectator).getLocation().getBlockZ() < -50) {
					Location loc = new Location(Bukkit.getPlayer(gm.getPlayers().get(0)).getWorld(), 0, 100, 0);
					Bukkit.getPlayer(spectator).teleport(loc);
				}
			}
		}
	}

	private void updatePlayerScoreboard() {
		for (Player allPlayers : Bukkit.getOnlinePlayers()) {
			if (ScoreboardUtil.hasScoreboard(allPlayers)) {
				ScoreboardUtil.updateTime(allPlayers);
			}
		}
	}

	private void subtractTimeBombTime(Map.Entry<Location, Integer> set) {
		if (set.getValue() >= 1) {
			set.setValue(set.getValue() - 1);
		}
	}

	private void blowUpChest(Map.Entry<Location, Integer> set) {
		if (set.getValue() == 0) {
			Bukkit.getWorld("uhc_world").createExplosion(set.getKey(), 4F, true);
			TimeBomb.removeTimeBombTime(set.getKey());
		}
	}

	private void announcementBeforeFinalHealMessage() {
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

	private void announcementFinalHealMessage() {
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

	private void announcementPvPEnabledMessage() {
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

	private void announcementDiscordMessage() {
		if (getFormattedTime().equalsIgnoreCase("7:00") || getFormattedTime().equalsIgnoreCase("17:00")
				|| getFormattedTime().equalsIgnoreCase("27:00") || getFormattedTime().equalsIgnoreCase("37:00")
				|| getFormattedTime().equalsIgnoreCase("47:00")) {
			Bukkit.broadcastMessage(u.messageFormat(
					"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
		}
	}

	private void borderShrinkMessage() {
		if (getFormattedTime().equalsIgnoreCase("30:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 500 500 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 500x500!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("35:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 250 250 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 250x250!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("40:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 100 100 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 100x100!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("45:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 50 50 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 50x50!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
		if (getFormattedTime().equalsIgnoreCase("50:00")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 25 25 0 0");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 25x25!", "a"));
			Bukkit.broadcastMessage("");
			playMessageSound();
		}
	}

	private void borderShrinkWarningMessage() {
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

	private void supplyDropCheck() {
		if (getFormattedTime().equalsIgnoreCase("10:00") || getFormattedTime().equalsIgnoreCase("15:00")
				|| getFormattedTime().equalsIgnoreCase("30:00")) {
			if (gm.isScenarioActive("SupplyDrops")) {
				int x = -501 + random.nextInt(501);
				int z = -501 + random.nextInt(501);
				int y = 90;
				SupplyDrops.spawnSupplyDrop(x, y, z);
				Bukkit.broadcastMessage(u.messageFormat(
						"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
			}
		}
	}

	private void playMessageSound() {
		for (UUID playerUUID : gm.getPlayers()) {
			if (Bukkit.getPlayer(playerUUID) != null) {
				Bukkit.getWorld("uhc_world").playSound(Bukkit.getPlayer(playerUUID).getLocation(),
						Sound.FIREWORK_LAUNCH, 3.0F, 0.533F);
			}
		}
	}

}