package com.iangongwer.scenarios;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import com.iangongwer.game.GameManager;

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

    public static void insertTimeBombTime(Location loc) {
        timeBombTime.put(loc, 30);
    }

    public static void removeTimeBombTime(Location loc) {
        timeBombTime.remove(loc);
    }

}