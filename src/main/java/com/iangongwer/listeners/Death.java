package com.iangongwer.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
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
import com.iangongwer.scenarios.TimeBomb;
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
			gm.storeDeathInventories(playerUUID, event.getDrops());
			gm.getDeathLocations().put(playerUUID, player.getLocation());
			event.setKeepInventory(true);

			// Spawn TimeBomb chest with items and start TimeBomb countdown
			if (gm.isScenarioActive("TimeBomb")) {
				TimeBomb.insertTimeBombTime(event.getEntity().getLocation());
				Block deathBlock = event.getEntity().getLocation().getBlock();
				Block deathBlock2 = Bukkit.getWorld("uhc_world").getBlockAt(deathBlock.getX() + 1, deathBlock.getY(),
						deathBlock.getZ());

				deathBlock.setType(Material.CHEST);
				deathBlock2.setType(Material.CHEST);

				Chest chest1 = (Chest) deathBlock.getState();

				chest1.getInventory().addItem(createGoldenHead(player));
				for (ItemStack item : gm.getDeathInventory(playerUUID)) {
					chest1.getInventory().addItem(item);
				}
			} else {
				player.getWorld().dropItemNaturally(gm.getDeathLocations().get(playerUUID), createGoldenHead(player));
				for (ItemStack item : gm.getDeathInventory(playerUUID)) {
					player.getWorld().dropItemNaturally(gm.getDeathLocations().get(playerUUID), item);
				}
			}

			if (!gm.isPvPEnabled()) {
				gm.getAlreadyScatteredPlayers().remove(player.getUniqueId());
				player.sendMessage(u.messageFormat("Use /latescatter for another chance at winning.", "a"));
			}

			if (killer instanceof Player && killer != null) {
				addKillOnPlayerKill(event, player, killer);
				dbm.addLiveKill(playerUUID, killer.getUniqueId());
			} else {
				event.setDeathMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " has been killed");
				dbm.addLiveKill(playerUUID, null);
			}

			addDeathOnPlayerDeath(player);
			WorldUtil.spawnFireworks(gm.getDeathLocations().get(playerUUID), 2);
			u.makeSpectator(player);
			dbm.setPlayersLeft();
			gm.isGameFinished();
		}
	}

	public void playerDeathInLobby(PlayerDeathEvent event, Player player) {
		if (GameState.isLobby()) {
			event.setDeathMessage("");
			event.setKeepInventory(true);
			player.getWorld().dropItemNaturally(player.getLocation(), createGoldenHead(player));
			player.spigot().respawn();
		}
	}

	public ItemStack createGoldenHead(Player player) {
		ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta gapMeta = goldenApple.getItemMeta();
		gapMeta.setDisplayName("Golden Head");
		goldenApple.setItemMeta(gapMeta);
		return goldenApple;
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