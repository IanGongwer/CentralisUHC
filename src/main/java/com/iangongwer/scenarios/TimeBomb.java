package com.iangongwer.scenarios;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.iangongwer.game.GameManager;
import com.iangongwer.game.GameState;

public class TimeBomb implements Listener {

    // Singleton of GameManager class
    private static TimeBomb single_instance = null;

    public static TimeBomb getInstance() {
        if (single_instance == null) {
            single_instance = new TimeBomb();
        }
        return single_instance;
    }

    GameManager gm = GameManager.getInstance();

    public static Map<Location, Integer> timeBombTime = new HashMap<Location, Integer>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (gm.isScenarioActive("TimeBomb")) {
            if (GameState.isInGame()) {
                insertTimeBombTime(event.getEntity().getLocation());
                Block deathBlock = event.getEntity().getLocation().getBlock();
                Block deathBlock2 = Bukkit.getWorld("uhc_world").getBlockAt(deathBlock.getX() + 1, deathBlock.getY(),
                        deathBlock.getZ());
                deathBlock.setType(Material.CHEST);
                deathBlock2.setType(Material.CHEST);

                Chest chest1 = (Chest) deathBlock.getState();
                Chest chest2 = (Chest) deathBlock2.getState();

                int counter = 0;
                for (ItemStack item : gm.getDeathInventory(event.getEntity().getUniqueId())) {
                    if (counter < 27) {
                        chest1.getInventory().addItem(item);
                        counter++;
                    } else {
                        chest2.getInventory().addItem(item);
                    }
                }
            }
        }
    }

    public void insertTimeBombTime(Location loc) {
        timeBombTime.put(loc, 30);
    }

    public void removeTimeBombTime(Location loc) {
        timeBombTime.remove(loc);
    }

}