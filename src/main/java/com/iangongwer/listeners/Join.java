package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.utils.HeartUtil;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;

public class Join implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		createPlayerOnJoin(playerUUID);
		LobbyUtil.joinLobbyUtil(player);
		inGamePlayerJoin(player, playerUUID);
	}

	private void createPlayerOnJoin(UUID playerUUID) {
		if (!Main.isRedisEnabled()) {
			dbm.createPlayer(playerUUID);
		} else {
			cr.createPlayer(playerUUID);
		}
	}

	public void inGamePlayerJoin(Player player, UUID playerUUID) {
		if (GameState.isInGame()) {
			player.sendMessage(Util.getInstance().messageFormat(
					"[UHC] Welcome to Centralis UHC. The game is currently in progress",
					"a"));

			if (gm.isPlayer(playerUUID)) {
				ScoreboardUtil.createGameScoreboard(player);
				HeartUtil.showHealth(player, ScoreboardUtil.getScoreboard(player).getScoreboard(),
						ScoreboardUtil.getScoreboard(player).getName());
				if (!gm.isPvPEnabled()) {
					if (GameManager.playersNotJoinedBack.contains(playerUUID)) {
						GameManager.playersNotJoinedBack.remove(playerUUID);
						GameManager.deathInventories.remove(playerUUID);
						gm.getDeathLocations().remove(playerUUID);
					}

					// QuitLogRunnable.dontkill.add(player.getUniqueId());
					// WorldUtil.despawnVillager(player);
					// QuitLogRunnable.dontkill.remove(player.getUniqueId());
					// Quit Log System Not Working
				} else {
					GameManager.quitLogTime.remove(playerUUID);
					player.sendMessage(Util.getInstance().messageFormat(
							"[UHC] You are now not quit-logged",
							"a"));
				}
			} else if (u.isInStaffMode(playerUUID)) {
				ScoreboardUtil.createStaffSpecScoreboard(player);
				Location loc = new Location(Bukkit.getPlayer(gm.getPlayers().get(0)).getWorld(), 0, 100, 0);
				player.teleport(loc);

			} else if (!gm.isPlayer(playerUUID)
					&& !u.isInStaffMode(playerUUID)) {
				u.makeSpectator(player);

				if (!gm.isPvPEnabled()) {
					player.sendMessage(Util.getInstance().messageFormat(
							"[UHC] Please use /latescatter, if you would like to join in the game",
							"a"));
				}
			}
		}
	}

}