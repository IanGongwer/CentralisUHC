package me.centy.uhc.scenarios;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.centy.uhc.game.GameManager;

public class Fireless implements Listener {

	@EventHandler
	public void onFireDamage(EntityDamageEvent event) {
		if (GameManager.getInstance().isScenarioActive("Fireless")) {
			if (event.getEntity() instanceof Player) {
				if (event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.FIRE)
						|| event.getCause().equals(DamageCause.FIRE_TICK)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onFireBlockDamage(EntityDamageByBlockEvent event) {
		if (GameManager.getInstance().isScenarioActive("Fireless")) {
			if (event.getEntity() instanceof Player) {
				if (event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.FIRE)
						|| event.getCause().equals(DamageCause.FIRE_TICK)) {
					event.setCancelled(true);
				}
			}
		}
	}

}