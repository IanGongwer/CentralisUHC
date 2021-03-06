package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.iangongwer.game.GameManager;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.utils.Util;

public class Fall implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onPlayerFall(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);
			player.teleport(GameManager.getInstance().getLobbySpawnPoint());
		}
		if (event.getCause() == DamageCause.FALL) {
			if (u.isLobby()) {
				event.setCancelled(true);
			}
			if (u.isScattering()) {
				event.setCancelled(true);
			}
			if (u.isInGame()) {
				if (GameRunnable.getSecondsPassed() <= 30) {
					event.setCancelled(true);
				} else {
					event.setCancelled(false);
				}
			}
			if (u.isEnd()) {
				event.setCancelled(true);
			}
		}
	}

}