package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.iangongwer.utils.Util;

public class FoodLevel implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
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
			event.setCancelled(true);
		}
	}

}