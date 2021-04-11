package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;
import me.centy.uhc.runnables.GameRunnable;

public class Fall implements Listener {

	@EventHandler
	public void onPlayerFall(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);
			player.teleport(GameManager.getInstance().getLobbySpawnPoint());
		}
		if (event.getCause() == DamageCause.FALL) {
			if (Util.getInstance().isLobby()) {
				event.setCancelled(true);
			}
			if (Util.getInstance().isScattering()) {
				event.setCancelled(true);
			}
			if (Util.getInstance().isInGame()) {
				if (GameRunnable.getSecondsPassed() <= 20) {
					event.setCancelled(true);
				} else {
					event.setCancelled(false);
				}
			}
			if (Util.getInstance().isEnd()) {
				event.setCancelled(true);
			}
		}
	}

}