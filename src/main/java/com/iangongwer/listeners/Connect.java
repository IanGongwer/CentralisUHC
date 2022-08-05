package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.iangongwer.commands.BanPlayerCommand;
import com.iangongwer.game.GameState;
import com.iangongwer.utils.Util;

public class Connect implements Listener {

	Util u = Util.getInstance();

	@EventHandler
	public void onPlayerConnect(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() || player.hasPermission("uhc.staff")) {
			event.allow();
		}
		if (u.getWhitelistStatus()) {
			if (!player.isOp() && !player.hasPermission("uhc.staff")) {
				if (!BanPlayerCommand.bannedPlayers.contains(player.getUniqueId())) {
					event.disallow(Result.KICK_OTHER,
							u.messageFormat("The game is currently whitelisted. Please wait to connect.", "c"));
				} else {
					event.disallow(Result.KICK_OTHER, u.messageFormat("You are currently banned.", "c"));
				}
			}
		}
		if (!u.getWhitelistStatus()) {
			if (!BanPlayerCommand.bannedPlayers.contains(player.getUniqueId())) {
				if (GameState.isLobby()) {
					event.allow();
				}
				if (GameState.isScattering()) {
					event.disallow(Result.KICK_OTHER,
							u.messageFormat("The game is currently in scattering mode. Please wait to connect.", "c"));
				}
				if (GameState.isInGame()) {
					event.allow();
				}
				if (GameState.isEnd()) {
					event.disallow(Result.KICK_OTHER, u.messageFormat("The game is currrently in ending mode.", "c"));
				}
			} else {
				event.disallow(Result.KICK_OTHER, u.messageFormat("You are currently banned.", "c"));
			}
		}
	}

}