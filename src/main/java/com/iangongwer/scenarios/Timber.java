package com.iangongwer.scenarios;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.iangongwer.game.GameManager;

public class Timber implements Listener {

	GameManager gm = GameManager.getInstance();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (gm.isScenarioActive("Timber")) {
			Block block = e.getBlock();
			block = block.getRelative(BlockFace.UP);
			while (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
				block.breakNaturally();
				block = block.getRelative(BlockFace.UP);
			}
		}
	}

}