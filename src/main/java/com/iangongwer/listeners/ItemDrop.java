package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class ItemDrop implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (GameState.isLobby() || GameState.isScattering()) {
			event.setCancelled(true);
		} else if (GameState.isInGame() || GameState.isEnd()) {
			event.setCancelled(false);
		}
	}

}