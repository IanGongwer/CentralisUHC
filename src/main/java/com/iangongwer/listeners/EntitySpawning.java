package com.iangongwer.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class EntitySpawning implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (GameState.isLobby()) {
			event.setCancelled(true);
		}
		if (GameState.isScattering()) {
			event.setCancelled(true);
		}
		if (GameState.isInGame()) {
			event.setCancelled(false);
		}
		if (GameState.isEnd()) {
			if (!event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
				event.setCancelled(true);
			}
		}
	}

}