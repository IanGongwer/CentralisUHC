package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class Interact implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (GameState.isLobby()) {
			if (LobbyUtil.isPracticePlayer(player.getUniqueId())) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
		if (GameState.isScattering()) {
			event.setCancelled(true);
		}
		if (GameState.isInGame()) {
			if (gm.isSpectator(player.getUniqueId()) || u.isInStaffMode(player.getUniqueId())) {
				event.setCancelled(true);
			} else {
				event.setCancelled(false);
			}
		}
		if (GameState.isEnd()) {
			event.setCancelled(false);
		}
	}

}