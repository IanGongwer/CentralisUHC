package com.iangongwer.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.utils.Util;

public class EndRunnable extends BukkitRunnable {

	private int uptimeSeconds = 0;

	@Override
	public void run() {
		if (Util.getInstance().isEnd()) {
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