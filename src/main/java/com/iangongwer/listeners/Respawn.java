package com.iangongwer.listeners;

import java.util.UUID;

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
			event.setRespawnLocation(LobbyUtil.getLobbySpawnPoint());
			if (LobbyUtil.isPracticePlayer(player.getUniqueId())) {
				event.setRespawnLocation(LobbyUtil.getPracticeSpawnPoint());
				LobbyUtil.practiceInventory(player);
			}
		}
		if (GameState.isInGame() || GameState.isEnd()) {
			for (UUID playerUUID : gm.getPlayers()) {
				if (Bukkit.getPlayer(playerUUID) != null) {
					event.setRespawnLocation(Bukkit.getPlayer(playerUUID).getLocation());
				}
			}
		}
	}

}