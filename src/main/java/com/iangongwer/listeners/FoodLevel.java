package com.iangongwer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class FoodLevel implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (GameState.isInGame()) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

}