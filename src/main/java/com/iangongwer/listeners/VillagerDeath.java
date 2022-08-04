package com.iangongwer.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;
import com.iangongwer.runnables.QuitLogRunnable;
import com.iangongwer.team.TeamManager;
import com.iangongwer.utils.Util;
import com.iangongwer.utils.WorldUtil;

public class VillagerDeath implements Listener {

	Util u = Util.getInstance();
	GameManager gm = GameManager.getInstance();
	TeamManager tm = TeamManager.getInstance();

	@EventHandler
	public void villagerDied(EntityDeathEvent event) {
		if (event.getEntityType().equals(EntityType.VILLAGER)) {

			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(event.getEntity().getCustomName());
			UUID playerUUID = player.getUniqueId();

			if (event.getEntity().getCustomName() != null && !QuitLogRunnable.dontkill.contains(playerUUID)) {
				Location loc = new Location(event.getEntity().getWorld(), event.getEntity().getLocation().getBlockX(),
						event.getEntity().getLocation().getBlockY() + 1, event.getEntity().getLocation().getBlockZ());
				ArrayList<ItemStack> item_list = gm.getQuitLoggedInventory(playerUUID);

				for (ItemStack item : item_list) {
					event.getEntity().getWorld().dropItemNaturally(loc, item);
				}

				WorldUtil.spawnFireworks(loc, 2);
				gm.removePlayer(playerUUID);
				gm.addSpectator(playerUUID);

				if (tm.areTeamsEnabled()) {
					tm.addDeceasedMember(playerUUID);
					tm.isFullTeamDead(playerUUID);
				}

				gm.isGameFinishedVillager();
			}
		}
	}

}