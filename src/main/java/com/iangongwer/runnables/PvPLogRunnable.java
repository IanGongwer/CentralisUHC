package com.iangongwer.runnables;

import java.util.Collections;
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
		for (Map.Entry<UUID, Integer> pvpLogList : gm.getPvPLogMap().entrySet()) {
			subtractPvPLogTime(pvpLogList);
			removePvPLoggedPlayer(pvpLogList);
		}
	}

	public void subtractPvPLogTime(Entry<UUID, Integer> pvpLogList) {
		if (pvpLogList.getValue() > 0) {
			gm.setPvPLogTime(pvpLogList.getKey(), pvpLogList.getValue() - 1);
		}
	}

	public void removePvPLoggedPlayer(Entry<UUID, Integer> pvpPlayerObject) {
		if (pvpPlayerObject.getValue() == 0) {
			Bukkit.getPlayer(pvpPlayerObject.getKey())
					.sendMessage(Util.getInstance().messageFormat("You are not PvP logged anymore", "a"));
			gm.getPvPLogMap().keySet().removeAll(Collections.singleton(pvpPlayerObject.getKey()));
		}
	}

	public void killPvPLogPlayerProcedure(UUID playerUUID) {
		gm.removePlayer(playerUUID);
		gm.setPvPLogTime(playerUUID, -1);
		gm.getPvPLogMap().keySet().removeAll(Collections.singleton(playerUUID));

		if (TeamManager.getInstance().areTeamsEnabled()) {
			tm.addDeceasedMember(playerUUID);
			tm.isFullTeamDead(playerUUID);
		}
	}

	public boolean isPvPLogged(UUID playerUUID) {
		return gm.getPvPLogMap().containsKey(playerUUID);
	}

}