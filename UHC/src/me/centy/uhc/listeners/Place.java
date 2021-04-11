package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Place implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
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
		if (player.isOp()) {
			event.setCancelled(false);
		}
	}

}