package com.iangongwer.runnables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.scenarios.TimeBomb;

public class EndRunnable extends BukkitRunnable {

	private static int uptimeSeconds = 0;

	GameManager gm = GameManager.getInstance();
	TimeBomb tb = TimeBomb.getInstance();

	static ArrayList<Map.Entry<Location, Integer>> listOfSets = new ArrayList<Map.Entry<Location, Integer>>();

	@Override
	public void run() {
		if (GameState.isEnd()) {
			if (gm.isScenarioActive("TimeBomb")) {
				for (Map.Entry<Location, Integer> set : TimeBomb.timeBombTime.entrySet()) {
					subtractTimeBombTime(set);
				}
				blowUpChest();
			}
			uptimeSeconds++;
			shutdownProcedure();
		}

	}

	private static void shutdownProcedure() {
		if (uptimeSeconds == 30) {
			for (Player allPlayers : Bukkit.getOnlinePlayers()) {
				allPlayers.kickPlayer("Thank you for playing. Discord: discord.centralis.cc");
			}
			Bukkit.getServer().shutdown();
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

}