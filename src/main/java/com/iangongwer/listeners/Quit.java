package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;

public class Quit implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		UUID player = event.getPlayer().getUniqueId();
		event.setQuitMessage("");
		if (u.isLobby()) {
			if (tm.hasTeam(player)) {
				if (tm.getTeamLeader(player) == player) {
					tm.deleteTeam(player);
				}
			}
			gm.removePlayer(player);
			if (u.isPracticePlayer(player)) {
				u.removePracticePlayer(player);
			}
		}
		if (u.isScattering()) {
			gm.removePlayer(player);
			if (tm.hasTeam(player)) {
				if (tm.getTeamLeader(player) == player) {
					tm.deleteTeam(player);
				}
			}
		}
		if (u.isInGame()) {
			if (gm.isSpectator(player)) {
				gm.removeSpectator(player);
			}
			if (!gm.isSpectator(player)) {
				gm.setQuitLogTime(player, 150);
				gm.addQuitLoggedPlayer(player);
				gm.storeQuitLoggedInventories(player);
				u.spawnVillager(Bukkit.getPlayer(player));
			}

		}
		if (u.isEnd()) {
			gm.removeSpectator(player);
			gm.removePlayer(player);
		}
	}

}