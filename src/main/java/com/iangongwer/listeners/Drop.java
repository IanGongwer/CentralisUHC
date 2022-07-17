package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.iangongwer.utils.Util;

public class Drop implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (u.isLobby()) {
			event.setCancelled(true);
		}
		if (u.isScattering()) {
			event.setCancelled(true);
		}
		if (u.isInGame()) {
			event.setCancelled(false);
		}
		if (u.isEnd()) {
			event.setCancelled(false);
		}
	}

}