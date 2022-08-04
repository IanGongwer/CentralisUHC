package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.iangongwer.game.GameState;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;

public class Fall implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onPlayerFall(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);
			player.teleport(LobbyUtil.getLobbySpawnPoint());
		}
		if (event.getCause() == DamageCause.FALL) {
			if (GameState.isInGame()) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
		}
	}

}