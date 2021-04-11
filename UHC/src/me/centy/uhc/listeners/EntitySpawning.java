package me.centy.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import me.centy.uhc.game.Util;

public class EntitySpawning implements Listener {

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (Util.getInstance().isLobby()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isScattering()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isInGame()) {
			event.setCancelled(false);
		}
		if (Util.getInstance().isEnd()) {
			event.setCancelled(true);
		}
	}

}