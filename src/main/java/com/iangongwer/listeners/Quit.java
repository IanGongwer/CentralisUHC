package com.iangongwer.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.runnables.PvPLogRunnable;
import com.iangongwer.scenarios.TimeBomb;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.LobbyUtil;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.WorldUtil;

public class Quit implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();
	PvPLogRunnable pr = new PvPLogRunnable();

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		UUID playerUUID = event.getPlayer().getUniqueId();

		if (GameState.isLobby()) {
			deleteTeamIfLeaderQuits(playerUUID);
			gm.removePlayer(playerUUID);

			if (LobbyUtil.isPracticePlayer(playerUUID)) {
				LobbyUtil.removePracticePlayer(playerUUID);
			}
		}

		if (GameState.isScattering()) {
			gm.removePlayer(playerUUID);
			gm.removeScatteredPlayer(playerUUID);
			deleteTeamIfLeaderQuits(playerUUID);
		}

		if (GameState.isInGame()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				if (pr.isPvPLogged(playerUUID)) {
					List<ItemStack> playerQuitInventory1 = new ArrayList<ItemStack>();
					for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getContents()) {
						playerQuitInventory1.add(item);
					}
					gm.storeDeathInventories(playerUUID,
							playerQuitInventory1);
					gm.getDeathLocations().put(playerUUID, Bukkit.getPlayer(playerUUID).getLocation());

					// Spawn TimeBomb chest with items and start TimeBomb countdown
					if (gm.isScenarioActive("TimeBomb")) {
						TimeBomb.insertTimeBombTime(Bukkit.getPlayer(playerUUID).getLocation());
						Block deathBlock = Bukkit.getPlayer(playerUUID).getLocation().getBlock();
						Block deathBlock2 = Bukkit.getWorld("uhc_world").getBlockAt(deathBlock.getX() + 1,
								deathBlock.getY(),
								deathBlock.getZ());

						deathBlock.setType(Material.CHEST);
						deathBlock2.setType(Material.CHEST);

						Chest chest1 = (Chest) deathBlock.getState();

						chest1.getInventory().addItem(createGoldenHead(Bukkit.getPlayer(playerUUID)));
						for (ItemStack item : gm.getDeathInventory(playerUUID)) {
							chest1.getInventory().addItem(item);
						}
					} else {
						Bukkit.getPlayer(playerUUID).getWorld().dropItemNaturally(
								gm.getDeathLocations().get(playerUUID),
								createGoldenHead(Bukkit.getPlayer(playerUUID)));
						for (ItemStack item : gm.getDeathInventory(playerUUID)) {
							Bukkit.getPlayer(playerUUID).getWorld()
									.dropItemNaturally(gm.getDeathLocations().get(playerUUID), item);
						}
					}

					WorldUtil.spawnFireworks(gm.getDeathLocations().get(playerUUID), 2);
					pr.killPvPLogPlayerProcedure(playerUUID);
					Bukkit.broadcastMessage(ChatColor.GREEN + Bukkit.getPlayer(playerUUID).getDisplayName()
							+ ChatColor.WHITE + " has been killed");

					ConnectionMYSQL.getInstance().setPlayersLeft();
				} else {
					gm.getAlreadyScatteredPlayers().remove(playerUUID);
					List<ItemStack> playerQuitInventory1 = new ArrayList<ItemStack>();
					for (ItemStack item : Bukkit.getPlayer(playerUUID).getInventory().getContents()) {
						playerQuitInventory1.add(item);
					}
					gm.storeDeathInventories(playerUUID,
							playerQuitInventory1);
					gm.getDeathLocations().put(playerUUID, Bukkit.getPlayer(playerUUID).getLocation());
					if (gm.isPvPEnabled()) {
						GameManager.quitLogTime.put(playerUUID, 300);
					} else {
						GameManager.playersNotJoinedBack.add(playerUUID);
					}
				}
			}
			gm.isGameFinished();
		}

		if (GameState.isEnd()) {
			if (gm.isSpectator(playerUUID)) {
				gm.removeSpectator(playerUUID);
			} else if (u.isInStaffMode(playerUUID)) {
				u.removeStaffMode(playerUUID);
			} else if (gm.isPlayer(playerUUID)) {
				gm.removePlayer(playerUUID);
			}
		}
	}

	public void deleteTeamIfLeaderQuits(UUID playerUUID) {
		if (tm.hasTeam(playerUUID)) {
			if (tm.getTeamLeader(playerUUID) == playerUUID) {
				tm.deleteTeam(playerUUID);
			}
		}
	}

	public ItemStack createGoldenHead(Player player) {
		ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta gapMeta = goldenApple.getItemMeta();
		gapMeta.setDisplayName("Golden Head");
		goldenApple.setItemMeta(gapMeta);
		return goldenApple;
	}

}