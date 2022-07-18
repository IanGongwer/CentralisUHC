package com.iangongwer.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.iangongwer.game.GameManager;
import com.iangongwer.holograms.LobbyHolograms;
import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.runnables.QuitLogRunnable;
import com.iangongwer.utils.Util;

public class Join implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player joinedPlayer = event.getPlayer();
		if(!Main.isRedisEnabled()) {
			dbm.createPlayer(joinedPlayer.getUniqueId());
		} else {
			cr.createPlayer(joinedPlayer.getUniqueId());
		}

		if (Bukkit.getOnlinePlayers().size() == 1) {
			LobbyHolograms lbho = new LobbyHolograms();
			lbho.createLobbyHologram();
		}
		if (u.isLobby()) {
			u.joinLobbyUtil(joinedPlayer);
		}
		if (u.isInGame()) {
			joinedPlayer.sendMessage("Welcome to Centralis UHC. The game is currently in progress.");
			if (gm.getPlayers().contains(joinedPlayer.getUniqueId())) {
				u.createGameScoreboard(joinedPlayer);
				gm.setQuitLogTime(joinedPlayer.getUniqueId(), -1);
				gm.removeQuitLoggedPlayer(joinedPlayer.getUniqueId());
				QuitLogRunnable.dontkill.add(joinedPlayer.getUniqueId());
				u.despawnVillager(joinedPlayer);
			} else if (u.isInStaffMode(joinedPlayer.getUniqueId())) {
				u.createStaffSpecScoreboard(joinedPlayer);
			} else if (gm.isSpectator(joinedPlayer.getUniqueId())) {
				u.makeSpectator(joinedPlayer);
			}
			if (!gm.isPvPEnabled()) {
				joinedPlayer.sendMessage("Use /latescatter to join.");
			}
		}
	}

}