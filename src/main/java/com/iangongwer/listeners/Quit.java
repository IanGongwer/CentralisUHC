package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class Quit implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		UUID playerUUID = event.getPlayer().getUniqueId();

		if (GameState.isLobby()) {
			if (tm.hasTeam(playerUUID)) {
				if (tm.getTeamLeader(playerUUID) == playerUUID) {
					tm.deleteTeam(playerUUID);
				}
			}

			gm.removePlayer(playerUUID);

			if (LobbyUtil.isPracticePlayer(playerUUID)) {
				LobbyUtil.removePracticePlayer(playerUUID);
			}
		}

		if (GameState.isScattering()) {
			gm.removePlayer(playerUUID);
			gm.removeScatteredPlayer(playerUUID);

			if (tm.hasTeam(playerUUID)) {
				if (tm.getTeamLeader(playerUUID) == playerUUID) {
					tm.deleteTeam(playerUUID);
				}
			}
		}

		if (GameState.isInGame()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				// gm.setQuitLogTime(playerUUID, 150);
				// gm.addQuitLoggedPlayer(playerUUID);
				// gm.storeQuitLoggedInventories(playerUUID);
				// WorldUtil.spawnVillager(Bukkit.getPlayer(playerUUID));
			}

		}

		if (GameState.isEnd()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				gm.removePlayer(playerUUID);
			}
		}
	}

}