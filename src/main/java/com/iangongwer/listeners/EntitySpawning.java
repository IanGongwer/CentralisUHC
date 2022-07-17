package com.iangongwer.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.iangongwer.utils.Util;

public class EntitySpawning implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (u.isLobby()) {
			if (event.getEntityType().equals(EntityType.ARMOR_STAND)) {
				event.setCancelled(false);
				return;
			} else {
				event.setCancelled(true);
				return;
			}
		}
		if (u.isScattering()) {
			event.setCancelled(true);
		}
		if (u.isInGame()) {
			event.setCancelled(false);
		}
		if (u.isEnd()) {
			if (!event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
				event.setCancelled(true);
			}
		}
	}

}