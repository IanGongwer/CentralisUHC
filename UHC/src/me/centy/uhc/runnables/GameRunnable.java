package me.centy.uhc.runnables;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class GameRunnable extends BukkitRunnable {

	private static int totalTime = 0;

	ArrayList<UUID> toRemove = new ArrayList<UUID>();

	@Override
	public void run() {
		if (Util.getInstance().isInGame()) {
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				if (Util.getInstance().hasScoreboard(allPlayers)) {
					Util.getInstance().updateTime(allPlayers);
				}
			}
			if (Bukkit.getServer().spigot().getTPS()[0] > 19.0) {
				totalTime++;
			}
			if (getFormattedTime().equalsIgnoreCase("5:00")) {
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Final heal is in 5 minutes!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("10:00")) {
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.setHealth(20.0);
				}
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("All players are now healed!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("15:00")) {
				GameManager.getInstance().setPvPEnabled(true);
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("PvP is now enabled!", "a"));
				for (UUID playerUUID : GameManager.getInstance().getPlayers()) {
					if (Bukkit.getPlayer(playerUUID) == null) {
						toRemove.add(playerUUID);
					}
				}
				GameManager.getInstance().getPlayers().removeAll(toRemove);
			}
			if (getFormattedTime().equalsIgnoreCase("30:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 250 250 0 0");
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Border is now 500x500!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("35:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 125 125 0 0");
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Border is now 250x250!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("40:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 50 50 0 0");
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Border is now 100x100!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("45:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 25 25 0 0");
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Border is now 50x50!", "a"));
			}
			if (getFormattedTime().equalsIgnoreCase("50:00")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world set 13 13 0 0");
				Bukkit.broadcastMessage(Util.getInstance().messageFormat("Border is now 25x25!", "a"));
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