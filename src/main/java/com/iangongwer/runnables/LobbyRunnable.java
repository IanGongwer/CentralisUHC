package com.iangongwer.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class LobbyRunnable extends BukkitRunnable {

	private static int totalTime = 0;

	GameManager gm = GameManager.getInstance();

	@Override
	public void run() {
		if (GameState.isLobby()) {
			if (!Util.getInstance().getWhitelistStatus()) {
				totalTime++;
				if (getFormattedTime().equals("8:00")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] The game is starting in 10 minutes",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("13:00")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] The game is starting in 5 minutes",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("15:00")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] The game is starting in 3 minutes",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("16:00")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] The game is starting in 2 minutes",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("16:30")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] The game is starting in 1 minute and 30 seconds",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
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