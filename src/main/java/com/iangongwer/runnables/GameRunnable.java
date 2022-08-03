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
import com.iangongwer.scenarios.TimeBomb;
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
		if (u.isInGame()) {
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				if (u.hasScoreboard(allPlayers)) {
					u.updateTime(allPlayers);
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
				Bukkit.broadcastMessage(u.messageFormat("Final heal is in 5 minutes!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("10:00")) {
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.setHealth(20.0);
				}
				Bukkit.broadcastMessage(u.messageFormat("All players are now healed!", "a"));
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -501 + random.nextInt(501);
					int z = -501 + random.nextInt(501);
					int y = 90;
					u.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
			}
			if (getFormattedTime().equalsIgnoreCase("15:00")) {
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -501 + random.nextInt(501);
					int z = -501 + random.nextInt(501);
					int y = 90;
					u.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
				gm.setPvPEnabled(true);
				Bukkit.broadcastMessage(u.messageFormat("PvP is now enabled!", "a"));
				for (UUID playerUUID : gm.getPlayers()) {
					if (Bukkit.getPlayer(playerUUID) == null) {
						toRemove.add(playerUUID);
					}
				}
				gm.getPlayers().removeAll(toRemove);
			}
			if (getFormattedTime().equalsIgnoreCase("30:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 500 500 0 0");
				Bukkit.broadcastMessage(u.messageFormat("Border is now 500x500!", "a"));
				if (gm.isScenarioActive("SupplyDrops")) {
					int x = -200 + random.nextInt(200);
					int z = -200 + random.nextInt(200);
					int y = 90;
					u.spawnSupplyDrop(x, y, z);
					Bukkit.broadcastMessage(u.messageFormat(
							"[Supply Drop] There is a supply drop at X: " + x + " Y: " + y + " Z: " + z, "a"));
				}
			}
			if (getFormattedTime().equalsIgnoreCase("35:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 250 250 0 0");
				Bukkit.broadcastMessage(u.messageFormat("Border is now 250x250!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("40:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 125 125 0 0");
				Bukkit.broadcastMessage(u.messageFormat("Border is now 100x100!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("45:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 50 50 0 0");
				Bukkit.broadcastMessage(u.messageFormat("Border is now 50x50!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("50:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 25 25 0 0");
				Bukkit.broadcastMessage(u.messageFormat("Border is now 25x25!", "a"));
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