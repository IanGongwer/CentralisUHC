package me.centy.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.centy.uhc.game.GameManager;
import me.centy.uhc.game.Util;

public class Join implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player joinedPlayer = event.getPlayer();
		if (Util.getInstance().isLobby()) {
			joinedPlayer.setGameMode(GameMode.SURVIVAL);
			joinedPlayer.getInventory().clear();
			joinedPlayer.getInventory().setArmorContents(null);
			joinedPlayer.setFoodLevel(20);
			joinedPlayer.setHealth(20.0);
			joinedPlayer.sendMessage("");
			joinedPlayer.sendMessage("Welcome to Centralis UHC. Use /prac to join the practice arena.");
			joinedPlayer.sendMessage("");
			joinedPlayer.teleport(gm.getLobbySpawnPoint());
			GameManager.getInstance().addPlayer(joinedPlayer.getUniqueId());
			Util.getInstance().createLobbyScoreboard(joinedPlayer);
		}
		if (Util.getInstance().isScattering()) {
			return;
		}
		if (Util.getInstance().isInGame()) {
			if (GameManager.getInstance().getPlayers().contains(joinedPlayer.getUniqueId())) {
				Util.getInstance().createGameScoreboard(joinedPlayer);
			}
			if (Util.getInstance().isInStaffMode(joinedPlayer.getUniqueId())) {
				Util.getInstance().createStaffSpecScoreboard(joinedPlayer);
			}
			joinedPlayer.sendMessage("Welcome to Centralis UHC. The game is currently in progress.");
			if (GameManager.getInstance().isSpectator(joinedPlayer.getUniqueId())) {
				if (!GameManager.getInstance().isPvPEnabled()) {
					joinedPlayer.sendMessage("Use /latescatter to join.");
				}
				Util.getInstance().createStaffSpecScoreboard(joinedPlayer);
				joinedPlayer.setGameMode(GameMode.SPECTATOR);
				joinedPlayer.setFoodLevel(20);
				joinedPlayer.setHealth(20.0);
				joinedPlayer.getInventory().clear();
				joinedPlayer.getInventory().setArmorContents(null);
				for (Player allPlayers : Bukkit.getOnlinePlayers()) {
					allPlayers.hidePlayer(joinedPlayer);
				}
			}
			/*
			 * if
			 * (GameManager.getInstance().getPlayers().contains(joinedPlayer.getUniqueId()))
			 * { List<Entity> entities = Bukkit.getWorld("uhc_world").getEntities(); for
			 * (Entity entity : entities) { if (entity instanceof Villager) { if
			 * (((Villager) entity).getCustomName()
			 * .equalsIgnoreCase(Bukkit.getPlayer(joinedPlayer.getUniqueId()).getDisplayName
			 * ())) { entities.remove(entity); } } } }
			 */
		}
		if (Util.getInstance().isEnd()) {
			return;
		}
	}

}