package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.iangongwer.game.GameManager;
import com.iangongwer.utils.Util;

public class Connect implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onPlayerConnect(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			event.allow();
		}
		if (u.getWhitelistStatus()) {
			if (!player.isOp()) {
				event.disallow(Result.KICK_OTHER,
						u.messageFormat("The game is currently whitelisted. Please wait to connect.", "c"));
			}
		}
		if (!u.getWhitelistStatus()) {
			if (u.isLobby()) {
				event.allow();
			}
			if (u.isScattering()) {
				event.disallow(Result.KICK_OTHER,
						u.messageFormat("The game is currently in scattering mode. Please wait to connect.", "c"));
			}
			if (u.isInGame()) {
				if (!GameManager.getInstance().getPlayers().contains(player.getUniqueId())) {
					GameManager.getInstance().addSpectator(player.getUniqueId());
				}
				event.allow();
			}
			if (u.isEnd()) {
				event.disallow(Result.KICK_OTHER, u.messageFormat("The game is currrently in ending mode.", "c"));
			}
		}
	}

}