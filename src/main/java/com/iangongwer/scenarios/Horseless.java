package com.iangongwer.scenarios;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

import com.iangongwer.game.GameManager;

public class Horseless implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onHorseRide(EntityMountEvent event) {
		if (gm.isScenarioActive("Horseless")) {
			if (event.getMount().getType().equals(EntityType.HORSE)) {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(false);
		}
	}

}