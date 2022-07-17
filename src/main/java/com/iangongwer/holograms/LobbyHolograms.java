package com.iangongwer.holograms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.iangongwer.main.Main;
import com.iangongwer.mysql.ConnectionMYSQL;
import com.iangongwer.utils.Util;

public class LobbyHolograms {

	Main main = Main.getInstance();
	ConnectionMYSQL dbm = ConnectionMYSQL.getInstance();
	Util u = Util.getInstance();

	Location loc = new Location(Bukkit.getWorld("world"), -240, 47, -640);

	public void createLobbyHologram() {
		for (Entity entity : Bukkit.getWorld("world").getEntities()) {
			if (entity.getType().equals(EntityType.ARMOR_STAND)) {
				entity.remove();
			}
		}

		// First Line
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName("Server Statistics");
		mostKills(loc);
		mostDeaths(loc);
		mostWins(loc);
	}

	public void mostKills(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName("Most Kills: " + Bukkit.getPlayer(dbm.getMostKillsUUID()).getDisplayName() + " has "
				+ dbm.getKills(dbm.getMostKillsUUID()) + " kills");
	}

	public void mostDeaths(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName("Most Deaths: " + Bukkit.getPlayer(dbm.getMostDeathsUUID()).getDisplayName() + " has "
				+ dbm.getDeaths(dbm.getMostDeathsUUID()) + " deaths");
	}

	public void mostWins(Location loc) {
		ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, -.35, 0), EntityType.ARMOR_STAND);
		hologram.setVisible(false);
		hologram.setGravity(false);
		hologram.setCustomNameVisible(true);
		hologram.setCustomName("Most Wins: " + Bukkit.getPlayer(dbm.getMostWinsUUID()).getDisplayName() + " has "
				+ dbm.getWins(dbm.getMostWinsUUID()) + " wins");
	}

}