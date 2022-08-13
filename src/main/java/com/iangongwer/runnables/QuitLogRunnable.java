package com.iangongwer.runnables;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.iangongwer.game.GameManager;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class QuitLogRunnable extends BukkitRunnable {

	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	ArrayList<Map.Entry<UUID, Integer>> toRemove = new ArrayList<Map.Entry<UUID, Integer>>();

	@Override
	public void run() {
		for (Map.Entry<UUID, Integer> quitLogList : GameManager.quitLogTime.entrySet()) {
			subtractQuitLogTime(quitLogList);
			removeQuitLoggedPlayer(quitLogList, quitLogList.getKey());
		}
		for (Map.Entry<UUID, Integer> toRemoveItem : toRemove) {
			GameManager.quitLogTime.remove(toRemoveItem.getKey());
		}
	}

	public void subtractQuitLogTime(Entry<UUID, Integer> quitLogList) {
		if (quitLogList.getValue() >= 1) {
			GameManager.quitLogTime.put(quitLogList.getKey(), quitLogList.getValue() - 1);
		}
	}

	public void removeQuitLoggedPlayer(Entry<UUID, Integer> quitLogList, UUID playerUUID) {
		if (quitLogList.getValue() == 0) {
			gm.removePlayer(playerUUID);
			GameManager.quitLogTime.put(playerUUID, -1);
			toRemove.add(quitLogList);
			Bukkit.broadcastMessage(Util.getInstance().messageFormat(
					"[UHC] " + Bukkit.getOfflinePlayer(playerUUID).getName() + " has been killed from quit-logging",
					"a"));

			if (TeamManager.getInstance().areTeamsEnabled()) {
				tm.addDeceasedMember(playerUUID);
				tm.isFullTeamDead(playerUUID);
			}
			gm.isGameFinished();
		}
	}

	public boolean isPvPLogged(UUID playerUUID) {
		return gm.getPvPLogMap().containsKey(playerUUID);
	}

}