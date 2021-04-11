package me.centy.uhc.scenarios;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.centy.uhc.game.GameManager;

public class CutClean implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		World blockWorld = block.getWorld();
		Location blockLocation = block.getLocation();
		Player player = event.getPlayer();
		if (!GameManager.getInstance().isScenarioActive("CutClean")) {
			if (block.getType() == Material.COAL_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.COAL, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.IRON_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.IRON_ORE, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.LAPIS_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.INK_SACK, 6, (short) 4));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.GOLD_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.GOLD_ORE, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.REDSTONE_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.REDSTONE, 6));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.DIAMOND_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.EMERALD_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
		}
		if (GameManager.getInstance().isScenarioActive("CutClean")) {
			if (block.getType() == Material.COAL_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.COAL, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.IRON_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.LAPIS_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.INK_SACK, 6, (short) 4));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.GOLD_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.REDSTONE_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.REDSTONE, 6));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.DIAMOND_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
			if (block.getType() == Material.EMERALD_ORE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
				ExperienceOrb orb = blockWorld.spawn(blockLocation, ExperienceOrb.class);
				orb.setExperience(1);
			}
		}

	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Sheep) {
			event.getDrops().add(new ItemStack(Material.STRING, 2));
		}
		if (GameManager.getInstance().isScenarioActive("CutClean")) {
			if (event.getEntity() instanceof Player) {
				return;
			}
			if (event.getEntity() instanceof Cow) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
				event.getDrops().add(new ItemStack(Material.LEATHER, 1));
			} else if (event.getEntity() instanceof Pig) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
			} else if (event.getEntity() instanceof Chicken) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 3));
				event.getDrops().add(new ItemStack(Material.FEATHER, 2));
			} else if (event.getEntity() instanceof Villager) {
				if (new Random().nextInt(99) < 50) {
					event.getDrops().clear();
					event.getDrops().add(new ItemStack(Material.BOOK, 1));
				}
			} else if (event.getEntity() instanceof Sheep) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.COOKED_MUTTON, 3));
			} else if (event.getEntity() instanceof Horse) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.LEATHER, 2));
			} else if (event.getEntity() instanceof PigZombie) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.GOLD_NUGGET, 1));
				event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, 1));
			} else if (event.getEntity() instanceof Spider || event.getEntity() instanceof CaveSpider) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.STRING, 2));
			} else if (event.getEntity() instanceof Zombie) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, 2));
			} else if (event.getEntity() instanceof Skeleton) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.ARROW, 2));
				event.getDrops().add(new ItemStack(Material.BONE, 1));
			} else if (event.getEntity() instanceof Creeper) {
				event.getDrops().clear();
				event.getDrops().add(new ItemStack(Material.SULPHUR, 2));
			}
		}
	}

}