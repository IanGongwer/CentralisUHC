package me.centy.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Quit implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage("");
		if (Util.getInstance().isLobby()) {
			GameManager.getInstance().removePlayer(player.getUniqueId());
			if (Util.getInstance().isPracticePlayer(player.getUniqueId())) {
				Util.getInstance().removePracticePlayer(player.getUniqueId());
			}
		}
		if (Util.getInstance().isScattering()) {
			GameManager.getInstance().removePlayer(player.getUniqueId());
		}
		if (Util.getInstance().isInGame()) {
			if (GameManager.getInstance().isSpectator(player.getUniqueId())) {
				GameManager.getInstance().removeSpectator(player.getUniqueId());
			}
			if (GameManager.getInstance().isPvPEnabled()) {
				if (GameManager.getInstance().getPlayers().contains(player.getUniqueId())) {
					GameManager.getInstance().removePlayer(player.getUniqueId());
					player.setHealth(0.0);
				}
			}
			GameManager.getInstance().isGameFinished();
			/*
			 * if (!GameManager.getInstance().isSpectator(player.getUniqueId()) &&
			 * GameManager.getInstance().isCombatLogged(player.getUniqueId())) { Villager
			 * combatLogNPC = (Villager) player.getWorld().spawnEntity(player.getLocation(),
			 * EntityType.VILLAGER); combatLogNPC.setCustomName(player.getDisplayName());
			 * combatLogNPC.setCustomNameVisible(true); }
			 */
		}
		if (Util.getInstance().isEnd()) {
			GameManager.getInstance().removeSpectator(player.getUniqueId());
			GameManager.getInstance().removePlayer(player.getUniqueId());
		}
	}

}