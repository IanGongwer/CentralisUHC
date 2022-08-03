package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class Place implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (GameState.isLobby()) {
			event.setCancelled(true);
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
			event.setCancelled(true);
		}
		if (player.isOp()) {
			event.setCancelled(false);
		}
	}

}