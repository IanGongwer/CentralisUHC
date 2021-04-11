package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Drop implements Listener {

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (Util.getInstance().isLobby()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isScattering()) {
			event.setCancelled(true);
		}
		if (Util.getInstance().isInGame()) {
			if (GameManager.getInstance().isSpectator(player.getUniqueId())
					|| Util.getInstance().isInStaffMode(player.getUniqueId())) {
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