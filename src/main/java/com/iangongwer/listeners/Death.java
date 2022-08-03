package com.iangongwer.listeners;

import java.util.ArrayList;

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
		Player killer = event.getEntity().getKiller();
		if (GameState.isLobby()) {
			event.setDeathMessage(ChatColor.GREEN + killer.getDisplayName() + ChatColor.WHITE + " has killed "
					+ ChatColor.GREEN + player.getDisplayName());
			ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
			ItemMeta gapMeta = goldenApple.getItemMeta();
			gapMeta.setDisplayName("Golden Head");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(player.getDisplayName() + "'s Head");
			gapMeta.setLore(lore);
			goldenApple.setItemMeta(gapMeta);
			event.getEntity().getWorld().dropItemNaturally(player.getLocation(), goldenApple);
			event.setKeepInventory(true);
		}
		if (!GameState.isLobby()) {
			if (!Main.isRedisEnabled()) {
				dbm.addDeath(player.getUniqueId());
			} else {
				cr.addDeath(player.getUniqueId());
			}
			if (!gm.isPvPEnabled()) {
				player.sendMessage(u.messageFormat("Use /latescatter for another chance.", "a"));
			}
			if (killer instanceof Player) {
				if (!Main.isRedisEnabled()) {
					dbm.addKill(killer.getUniqueId());
				} else {
					cr.addKill(killer.getUniqueId());
				}
				event.setDeathMessage(ChatColor.GREEN + killer.getDisplayName() + ChatColor.WHITE + " has killed "
						+ ChatColor.GREEN + player.getDisplayName());
				gm.setPlayerKills(killer.getUniqueId(), gm.getPlayerKills(killer.getUniqueId()) + 1);
				ScoreboardUtil.updateKills(killer);
			} else {
				event.setDeathMessage(ChatColor.GREEN + player.getDisplayName() + ChatColor.WHITE + " has been killed");
			}
			ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
			ItemMeta gapMeta = goldenApple.getItemMeta();
			gapMeta.setDisplayName("Golden Head");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(player.getDisplayName() + "'s Head");
			gapMeta.setLore(lore);
			goldenApple.setItemMeta(gapMeta);
			event.getEntity().getWorld().dropItemNaturally(player.getLocation(), goldenApple);
			if (!gm.isScenarioActive("TimeBomb")) {
				event.setKeepInventory(false);
			} else {
				event.setKeepInventory(true);
			}
			gm.storeDeathInventories(player.getUniqueId(), event.getDrops());
			gm.getDeathLocations().put(player.getUniqueId(), player.getLocation());

			if (tm.areTeamsEnabled()) {
				tm.addDeceasedMember(player.getUniqueId());
			}
			gm.removePlayer(player.getUniqueId());
			gm.addSpectator(player.getUniqueId());
			if (tm.areTeamsEnabled()) {
				tm.isFullTeamDead(player.getUniqueId());
			}
			WorldUtil.spawnFireworks(player.getLocation(), 2);
			if (killer == null) {
				gm.isGameFinishedVillager();
			} else {
				gm.isGameFinished(killer.getUniqueId());
			}
		}
	}

}