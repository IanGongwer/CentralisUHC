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

public class PvPLogRunnable extends BukkitRunnable {

	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	ArrayList<Map.Entry<UUID, Integer>> toRemove = new ArrayList<Map.Entry<UUID, Integer>>();

	@Override
	public void run() {
		for (Map.Entry<UUID, Integer> pvpLogList : gm.getPvPLogMap().entrySet()) {
			subtractPvPLogTime(pvpLogList);
			removePvPLoggedPlayer(pvpLogList);
		}
		for (Map.Entry<UUID, Integer> toRemoveItem : toRemove) {
			gm.getPvPLogMap().remove(toRemoveItem.getKey());
		}
	}

	public void subtractPvPLogTime(Entry<UUID, Integer> pvpLogList) {
		if (pvpLogList.getValue() >= 1) {
			gm.setPvPLogTime(pvpLogList.getKey(), pvpLogList.getValue() - 1);
		}
	}

	public void removePvPLoggedPlayer(Entry<UUID, Integer> pvpPlayerObject) {
		if (pvpPlayerObject.getValue() == 0) {
			if (Bukkit.getPlayer(pvpPlayerObject.getKey()) != null) {
				Bukkit.getPlayer(pvpPlayerObject.getKey())
						.sendMessage(Util.getInstance().messageFormat("You are not PvP logged anymore", "a"));
			}
			toRemove.add(pvpPlayerObject);
		}
	}

	public void killPvPLogPlayerProcedure(UUID playerUUID) {
		gm.removePlayer(playerUUID);
		gm.setPvPLogTime(playerUUID, -1);
		gm.getPvPLogMap().remove(playerUUID);

		if (TeamManager.getInstance().areTeamsEnabled()) {
			tm.addDeceasedMember(playerUUID);
			tm.isFullTeamDead(playerUUID);
		}
	}

	public boolean isPvPLogged(UUID playerUUID) {
		return gm.getPvPLogMap().containsKey(playerUUID);
	}

}