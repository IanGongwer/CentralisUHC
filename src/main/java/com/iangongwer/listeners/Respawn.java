package com.iangongwer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class Respawn implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (GameState.isLobby()) {
			event.setRespawnLocation(gm.getLobbySpawnPoint());
			if (LobbyUtil.isPracticePlayer(player.getUniqueId())) {
				event.setRespawnLocation(LobbyUtil.getPracticeSpawnPoint());
				LobbyUtil.practiceInventory(player);
			}
		}
		if (GameState.isScattering()) {
			return;
		}
		if (GameState.isInGame() || GameState.isEnd()) {
			event.setRespawnLocation(Bukkit.getPlayer(gm.getPlayers().get(0)).getLocation());
			if (gm.isSpectator(player.getUniqueId())) {
				u.makeSpectator(player);
			}
		}
	}

}