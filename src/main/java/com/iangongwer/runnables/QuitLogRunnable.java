package com.iangongwer.runnables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.team.TeamManager;

public class QuitLogRunnable extends BukkitRunnable {

	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	public static ArrayList<UUID> dontkill = new ArrayList<UUID>();

	@Override
	public void run() {
		Map<UUID, Integer> quitLogList = gm.getQuitLogMap();
		for (Entry<UUID, Integer> quitPlayerObject : gm.getQuitLogMap().entrySet()) {
			if (quitPlayerObject.getValue() > 0) {
				quitLogList.put(quitPlayerObject.getKey(), quitPlayerObject.getValue() - 1);
				for (LivingEntity entity : Bukkit.getWorld("uhc_world").getLivingEntities()) {
					if (entity instanceof Villager && entity.getCustomName() != null) {
						if (entity.getCustomName()
								.equalsIgnoreCase(Bukkit.getOfflinePlayer(quitPlayerObject.getKey()).getName())) {
							entity.damage(.1333);
						}
					}
				}
			}
			if (quitPlayerObject.getValue() == 0) {
				gm.removePlayer(quitPlayerObject.getKey());
				gm.addSpectator(quitPlayerObject.getKey());
				gm.setQuitLogTime(quitPlayerObject.getKey(), -1);
				gm.removeQuitLoggedPlayer(quitPlayerObject.getKey());
				
				if(TeamManager.getInstance().areTeamsEnabled()) {
					tm.addDeceasedMember(quitPlayerObject.getKey());
				}
			}
		}
	}

}