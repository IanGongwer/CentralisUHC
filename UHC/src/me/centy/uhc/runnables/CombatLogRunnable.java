package me.centy.uhc.runnables;

import org.bukkit.scheduler.BukkitRunnable;

public class CombatLogRunnable extends BukkitRunnable {

	@Override
	public void run() {

	}

	/*
	 * @Override public void run() { if (Util.getInstance().isInGame()) { if
	 * (GameManager.getInstance().getCombatLogMap().size() != 0) { for (Entry<UUID,
	 * Integer> playerUUID : GameManager.getInstance().getCombatLogMap().entrySet())
	 * { int time = playerUUID.getValue(); if (time != 0) { time = time - 1; } else
	 * { GameManager.getInstance().getCombatLogMap().remove(playerUUID.getValue(),
	 * playerUUID.getKey()); List<Entity> entities =
	 * Bukkit.getWorld("uhc_world").getEntities(); for (Entity entity : entities) {
	 * if (entity instanceof Villager) { if (((Villager) entity).getCustomName()
	 * .equalsIgnoreCase(Bukkit.getPlayer(playerUUID.getKey()).getDisplayName())) {
	 * entities.remove(entity); } } } } } } }
	 * 
	 * }
	 */

}