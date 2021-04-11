package me.centy.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Respawn implements Listener {

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Util.getInstance().isLobby()) {
			event.setRespawnLocation(GameManager.getInstance().getLobbySpawnPoint());
			if (Util.getInstance().isPracticePlayer(player.getUniqueId())) {
				event.setRespawnLocation(Util.getInstance().getPracticeSpawnPoint());
				Util.getInstance().practiceInventory(player);
			}
		}
		if (Util.getInstance().isScattering()) {
			return;
		}
		if (Util.getInstance().isInGame()) {
			event.setRespawnLocation(Bukkit.getPlayer(GameManager.getInstance().getPlayers().get(0)).getLocation());
			if (GameManager.getInstance().isSpectator(player.getUniqueId())) {
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.hidePlayer(player);
				}
				Util.getInstance().makeSpectator(player);
			}
		}
		if (Util.getInstance().isEnd()) {
			event.setRespawnLocation(Bukkit.getPlayer(GameManager.getInstance().getPlayers().get(0)).getLocation());
		}
	}

}