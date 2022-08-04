package com.iangongwer.runnables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
	ArrayList<UUID> toRemove = new ArrayList<UUID>();

	@Override
	public void run() {
		if (GameState.isInGame()) {
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				if (ScoreboardUtil.hasScoreboard(allPlayers)) {
					ScoreboardUtil.updateTime(allPlayers);
				}
			}
			totalTime++;
			if (gm.isScenarioActive("TimeBomb")) {
				for (Map.Entry<Location, Integer> set : TimeBomb.timeBombTime.entrySet()) {
					set.setValue(set.getValue() - 1);
					if (set.getValue() == 0) {
						Bukkit.getWorld("uhc_world").createExplosion(set.getKey(), 4F, true);
						tb.removeTimeBombTime(set.getKey());

					}
				}
			}
			if (getFormattedTime().equalsIgnoreCase("5:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Final heal is in 5 minutes!", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("7:00")) {
				Bukkit.broadcastMessage(u.messageFormat(
						"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("10:00")) {
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.setHealth(20.0);
				}
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] All players are now healed!", "a"));
				Bukkit.broadcastMessage("");
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -501 + random.nextInt(501);
					int z = -501 + random.nextInt(501);
					int y = 90;
					SupplyDrops.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
			}
			if (getFormattedTime().equalsIgnoreCase("15:00")) {
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -501 + random.nextInt(501);
					int z = -501 + random.nextInt(501);
					int y = 90;
					SupplyDrops.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
				gm.setPvPEnabled(true);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] PvP is now enabled!", "a"));
				Bukkit.broadcastMessage("");
				for (UUID playerUUID : gm.getPlayers()) {
					if (Bukkit.getPlayer(playerUUID) == null) {
						toRemove.add(playerUUID);
					}
				}
				gm.getPlayers().removeAll(toRemove);
			}
			if (getFormattedTime().equalsIgnoreCase("17:00")) {
				Bukkit.broadcastMessage(u.messageFormat(
						"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("27:00")) {
				Bukkit.broadcastMessage(u.messageFormat(
						"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("29:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 500x500 in 1 minute", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("30:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 500 500 0 0");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 500x500!", "a"));
				Bukkit.broadcastMessage("");
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -200 + random.nextInt(200);
					int z = -200 + random.nextInt(200);
					int y = 90;
					SupplyDrops.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
			}
			if (getFormattedTime().equalsIgnoreCase("34:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 250x250 in 1 minute", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("35:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 250 250 0 0");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 250x250!", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("37:00")) {
				Bukkit.broadcastMessage(u.messageFormat(
						"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("39:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 100x100 in 1 minute", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("40:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 125 125 0 0");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 100x100!", "a"));
				Bukkit.broadcastMessage("");
			}
			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) == null) {
					toRemove.add(playerUUID);
				}
			}
			gm.getPlayers().removeAll(toRemove);
			if (getFormattedTime().equalsIgnoreCase("44:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 50x50 in 1 minute", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("45:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 50 50 0 0");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 50x50!", "a"));
				Bukkit.broadcastMessage("");
			}
			if (getFormattedTime().equalsIgnoreCase("47:00")) {
				Bukkit.broadcastMessage(u.messageFormat(
						"[UHC] If you are enjoying the game, be sure to join the discord: discord.gg/YTEM7Dq", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("49:00")) {
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is shrinking to 25x25 in 1 minute", "a"));
				Bukkit.broadcastMessage("");
			}
			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) == null) {
					toRemove.add(playerUUID);
				}
			}
			gm.getPlayers().removeAll(toRemove);
			if (getFormattedTime().equalsIgnoreCase("50:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 25 25 0 0");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(u.messageFormat("[UHC] Border is now 25x25!", "a"));
				Bukkit.broadcastMessage("");
			}
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

}