package com.iangongwer.runnables;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.scenarios.TimeBomb;
import com.iangongwer.utils.Util;

public class EndRunnable extends BukkitRunnable {

	private int uptimeSeconds = 0;

	GameManager gm = GameManager.getInstance();
	TimeBomb tb = TimeBomb.getInstance();

	@Override
	public void run() {
		if (Util.getInstance().isEnd()) {
			if (gm.isScenarioActive("TimeBomb")) {
				for (Map.Entry<Location, Integer> set : TimeBomb.timeBombTime.entrySet()) {
					set.setValue(set.getValue() - 1);
					if (set.getValue() == 0) {
						Bukkit.getWorld("uhc_world").createExplosion(set.getKey(), 4F, true);
						tb.removeTimeBombTime(set.getKey());
					}
				}
			}
			uptimeSeconds++;
			if (uptimeSeconds == 30) {
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.kickPlayer("Thank you for playing. Discord: discord.centralis.cc");
				}
				Bukkit.getServer().shutdown();
			}
		}

	}

}