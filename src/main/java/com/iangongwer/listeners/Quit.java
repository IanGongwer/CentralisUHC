package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.WorldUtil;

public class Quit implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		UUID player = event.getPlayer().getUniqueId();
		event.setQuitMessage("");
		if (GameState.isLobby()) {
			if (tm.hasTeam(player)) {
				if (tm.getTeamLeader(player) == player) {
					tm.deleteTeam(player);
				}
			}
			gm.removePlayer(player);
			if (LobbyUtil.isPracticePlayer(player)) {
				LobbyUtil.removePracticePlayer(player);
			}
		}
		if (GameState.isScattering()) {
			gm.removePlayer(player);
			if (tm.hasTeam(player)) {
				if (tm.getTeamLeader(player) == player) {
					tm.deleteTeam(player);
				}
			}
		}
		if (GameState.isInGame()) {
			if (gm.isSpectator(player)) {
				gm.removeSpectator(player);
			}
			if (!gm.isSpectator(player)) {
				gm.setQuitLogTime(player, 150);
				gm.addQuitLoggedPlayer(player);
				gm.storeQuitLoggedInventories(player);
				WorldUtil.spawnVillager(Bukkit.getPlayer(player));
			}

		}
		if (GameState.isEnd()) {
			gm.removeSpectator(player);
			gm.removePlayer(player);
		}
	}

}