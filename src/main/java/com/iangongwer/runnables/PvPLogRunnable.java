package com.iangongwer.runnables;

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

	@Override
	public void run() {
		Map<UUID, Integer> pvpLogList = gm.getPvPLogMap();
		for (Entry<UUID, Integer> pvpPlayerObject : gm.getPvPLogMap().entrySet()) {
			subtractPvPLogTime(pvpLogList, pvpPlayerObject);
			removePvPLoggedPlayer(pvpPlayerObject);
		}
	}

	public void subtractPvPLogTime(Map<UUID, Integer> pvpLogList, Entry<UUID, Integer> pvpPlayerObject) {
		if (pvpPlayerObject.getValue() > 0) {
			gm.setPvPLogTime(pvpPlayerObject.getKey(), pvpPlayerObject.getValue() - 1);
		}
	}

	public void removePvPLoggedPlayer(Entry<UUID, Integer> pvpPlayerObject) {
		if (pvpPlayerObject.getValue() == 0) {
			Bukkit.getPlayer(pvpPlayerObject.getKey())
					.sendMessage(Util.getInstance().messageFormat("You are not PvP logged anymore", "a"));
			gm.setPvPLogTime(pvpPlayerObject.getKey(), -1);
			gm.removePvPLoggedPlayer(pvpPlayerObject.getKey());
		}
	}

	public void killPvPLogPlayerProcedure(UUID playerUUID) {
		gm.removePlayer(playerUUID);
		gm.setPvPLogTime(playerUUID, -1);
		gm.removePvPLoggedPlayer(playerUUID);

		if (TeamManager.getInstance().areTeamsEnabled()) {
			tm.addDeceasedMember(playerUUID);
			tm.isFullTeamDead(playerUUID);
		}
	}

	public boolean isPvPLogged(UUID playerUUID) {
		return gm.getPvPLogMap().containsKey(playerUUID);
	}

}