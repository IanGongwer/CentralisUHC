package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.redis.ConnectionRedis;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.ScoreboardUtil;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.WorldUtil;

public class Death implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	ConnectionRedis cr = ConnectionRedis.getInstance();

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		UUID playerUUID = player.getUniqueId();
		Player killer = event.getEntity().getKiller();

		playerDeathInLobby(event, player);

		if (!GameState.isLobby()) {
			addDeathOnPlayerDeath(player);
			WorldUtil.spawnFireworks(player.getLocation(), 2);
			u.makeSpectator(player);
			giveGoldenHead(player);
			gm.storeDeathInventories(playerUUID, event.getDrops());
			gm.getDeathLocations().put(playerUUID, player.getLocation());

			if (!gm.isPvPEnabled()) {
				player.sendMessage(u.messageFormat("Use /latescatter for another chance at winning.", "a"));
			}

			if (killer instanceof Player) {
				addKillOnPlayerKill(event, player, killer);
			} else {
				event.setDeathMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " has been killed");
			}

			if (!gm.isScenarioActive("TimeBomb")) {
				event.setKeepInventory(false);
			} else {
				event.setKeepInventory(true);
			}

			if (tm.areTeamsEnabled()) {
				tm.addDeceasedMember(playerUUID);
			}

			if (tm.areTeamsEnabled()) {
				tm.isFullTeamDead(playerUUID);
			}

			if (killer == null) {
				gm.isGameFinishedVillager();
			} else {
				gm.isGameFinished(killer.getUniqueId());
			}
		}
	}

	public void playerDeathInLobby(PlayerDeathEvent event, Player player) {
		if (GameState.isLobby()) {
			event.setDeathMessage("");
			event.setKeepInventory(true);
			giveGoldenHead(player);
			player.spigot().respawn();
		}
	}

	public void giveGoldenHead(Player player) {
		ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta gapMeta = goldenApple.getItemMeta();
		gapMeta.setDisplayName("Golden Head");
		goldenApple.setItemMeta(gapMeta);
		player.getWorld().dropItemNaturally(player.getLocation(), goldenApple);
	}

	public void addDeathOnPlayerDeath(Player player) {
		if (!Main.isRedisEnabled()) {
			dbm.addDeath(player.getUniqueId());
		} else {
			cr.addDeath(player.getUniqueId());
		}
	}

	public void addKillOnPlayerKill(PlayerDeathEvent event, Player player, Player killer) {
		if (!Main.isRedisEnabled()) {
			dbm.addKill(killer.getUniqueId());
		} else {
			cr.addKill(killer.getUniqueId());
		}
		event.setDeathMessage(ChatColor.GREEN + killer.getDisplayName() + ChatColor.WHITE + " has killed "
				+ ChatColor.GREEN + player.getDisplayName());
		gm.setPlayerKills(killer.getUniqueId(), gm.getPlayerKills(killer.getUniqueId()) + 1);
		ScoreboardUtil.updateKills(killer);
	}

}