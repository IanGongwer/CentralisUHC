package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import me.centy.uhc.game.Util;

public class FoodLevel implements Listener {

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		if (Util.getInstance().isLobby()) {
			if (player.getFoodLevel() == 20) {
				event.setCancelled(true);
			}
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