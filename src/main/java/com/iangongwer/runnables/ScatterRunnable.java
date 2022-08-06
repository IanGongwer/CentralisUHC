package com.iangongwer.runnables;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;

public class ScatterRunnable extends BukkitRunnable {

	private static int totalTime = 0;

	GameManager gm = GameManager.getInstance();

	@Override
	public void run() {
		if (GameState.isScattering()) {
			if (GameManager.doneScattering) {
				totalTime++;
				if (getFormattedTime().equals("0:10")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"[UHC] You can relog till PvP enables (15 mins). If you relog after PvP enables, you will become a spectator",
							"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:15")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] To view crafting recipes: http://centralis.cc/recipes.html", "a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:20")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] Final heal is at 10 minutes. PvP enables at 15 minutes. Meetup is at 50 minutes",
									"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:25")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] God Apples are disabled. Golden Heads give you 2 hearts, speed, & strength",
									"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:30")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] Trapping is allowed until the 50x50 border",
									"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:35")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] Nether is disabled. Bookshelves & Pearl Damage are enabled",
									"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:40")) {
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					Bukkit.broadcastMessage(
							Util.getInstance().messageFormat(
									"[UHC] Type in chat if you have any other questions",
									"a"));
					Bukkit.broadcastMessage(Util.getInstance().messageFormat(
							"",
							"a"));
					GameRunnable.playMessageSound();
				}
				if (getFormattedTime().equals("0:45")) {
					for (UUID playerUUID : gm.getPlayers()) {
						Bukkit.getPlayer(playerUUID).setWalkSpeed(.2f);
						Bukkit.getPlayer(playerUUID).removePotionEffect(PotionEffectType.BLINDNESS);
						Bukkit.getPlayer(playerUUID).removePotionEffect(PotionEffectType.JUMP);
						ScoreboardUtil.createGameScoreboard(Bukkit.getPlayer(playerUUID));

						GameManager.doneScattering = false;
						gm.startGame();
					}
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