package me.centy.uhc.scenarios;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.centy.uhc.game.GameManager;

public class Timber implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (GameManager.getInstance().isScenarioActive("Timber")) {
			Block block = e.getBlock();
			block = block.getRelative(BlockFace.UP);
			while (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
				block.breakNaturally();
				block = block.getRelative(BlockFace.UP);
			}
		}
	}

}