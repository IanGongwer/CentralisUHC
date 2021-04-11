package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Connect implements Listener {

	@EventHandler
	public void onPlayerConnect(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			event.allow();
		}
		if (Util.getInstance().getWhitelistStatus()) {
			if (!player.isOp()) {
				event.disallow(Result.KICK_OTHER, Util.getInstance()
						.messageFormat("The game is currently whitelisted. Please wait to connect.", "c"));
			}
		}
		if (!Util.getInstance().getWhitelistStatus()) {
			if (Util.getInstance().isLobby()) {
				event.allow();
			}
			if (Util.getInstance().isScattering()) {
				event.disallow(Result.KICK_OTHER, Util.getInstance()
						.messageFormat("The game is currently in scattering mode. Please wait to connect.", "c"));
			}
			if (Util.getInstance().isInGame()) {
				if (!GameManager.getInstance().getPlayers().contains(player.getUniqueId())) {
					GameManager.getInstance().addSpectator(player.getUniqueId());
				}
				event.allow();
			}
			if (Util.getInstance().isEnd()) {
				event.disallow(Result.KICK_OTHER,
						Util.getInstance().messageFormat("The game is currrently in ending mode.", "c"));
			}
		}
	}

}