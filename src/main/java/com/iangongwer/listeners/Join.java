package com.iangongwer.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.runnables.GameRunnable;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.WorldUtil;

public class Join implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player joinedPlayer = event.getPlayer();

		if (!Main.isRedisEnabled()) {
			dbm.createPlayer(joinedPlayer.getUniqueId());
		} else {
			cr.createPlayer(joinedPlayer.getUniqueId());
		}
		if (GameState.isLobby()) {
			gm.setPlayerKills(joinedPlayer.getUniqueId(), 0);
			LobbyUtil.joinLobbyUtil(joinedPlayer);
		}
		if (GameState.isInGame()) {
			joinedPlayer.sendMessage("Welcome to Centralis UHC. The game is currently in progress.");
			if (gm.getPlayers().contains(joinedPlayer.getUniqueId())) {
				ScoreboardUtil.createGameScoreboard(joinedPlayer);
				// QuitLogRunnable.dontkill.add(joinedPlayer.getUniqueId());
				// WorldUtil.despawnVillager(joinedPlayer);
				// QuitLogRunnable.dontkill.remove(joinedPlayer.getUniqueId());
			} else if (u.isInStaffMode(joinedPlayer.getUniqueId())) {
				ScoreboardUtil.createStaffSpecScoreboard(joinedPlayer);
				joinedPlayer.teleport(WorldUtil.getSpectatorSpawnPoint());
			} else if (!gm.getPlayers().contains(joinedPlayer.getUniqueId())
					&& !u.isInStaffMode(joinedPlayer.getUniqueId())) {
				u.makeSpectator(joinedPlayer);
				if (GameRunnable.getSecondsPassed() <= 900) {
					joinedPlayer.sendMessage("Please use /latescatter, if you would like to join in the game.");
				}
			}
		}
	}

}