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
			getWoodBlocksAround(BlockFace.UP, block);
			getWoodBlocksAround(BlockFace.DOWN, block);
			getWoodBlocksAround(BlockFace.NORTH, block);
			getWoodBlocksAround(BlockFace.SOUTH, block);
			getWoodBlocksAround(BlockFace.EAST, block);
			getWoodBlocksAround(BlockFace.WEST, block);
		}
	}

	public void getWoodBlocksAround(BlockFace blockface, Block block) {
		block = block.getRelative(blockface);
		while (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
			block.breakNaturally();
			block = block.getRelative(blockface);
		}
	}

}