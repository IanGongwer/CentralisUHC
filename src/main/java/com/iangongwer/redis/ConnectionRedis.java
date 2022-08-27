package com.iangongwer.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import com.iangongwer.main.Main;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ConnectionRedis {

    // Singleton of ConnectionRedis class
    private static ConnectionRedis single_instance;

    public static ConnectionRedis getInstance() {
        if (single_instance == null) {
            single_instance = new ConnectionRedis();
        }
        return single_instance;
    }

    FileConfiguration yml = Main.getInstance().getConfig();

    public static JedisPool pool;

    static Map<String, String> playerCreationMap = new HashMap<>();

    private boolean connectedSuccessfully = false;

    public void connectToRedis() {
        Main.setRedisEnabled(true);
        pool = new JedisPool(yml.getString("Redis.Host"), 11192);
        Jedis j = null;
        try {
            j = pool.getResource();
            // If you want to use a password, use
            j.auth(yml.getString("Redis.Password"));
            connectedSuccessfully = true;
        } catch (Exception e) {
            connectedSuccessfully = false;
        } finally {
            // Be sure to close it! It can and will cause memory leaks.
            if (j != null) {
                j.close();
            }
        }

        playerCreationMap.put("kills", "0");
        playerCreationMap.put("deaths", "0");
        playerCreationMap.put("wins", "0");
    }

    public void createPlayer(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        if (!jedis.exists(playerUUID.toString())) {
            jedis.hmset(playerUUID.toString(), playerCreationMap);
        }
        jedis.close();
    }

    public void addKill(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        jedis.hset(playerUUID.toString(), "kills", (Integer.valueOf(getKills(playerUUID) + 1)).toString());
        jedis.close();
    }

    public String getKills(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        return jedis.hget(playerUUID.toString(), "kills");
    }

    public void addDeath(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        jedis.hset(playerUUID.toString(), "deaths", (Integer.valueOf(getDeaths(playerUUID) + 1)).toString());
        jedis.close();
    }

    public String getDeaths(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        return jedis.hget(playerUUID.toString(), "deaths");
    }

    public void addWin(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        jedis.hset(playerUUID.toString(), "wins", (Integer.valueOf(getWins(playerUUID) + 1)).toString());
        jedis.close();
    }

    public String getWins(UUID playerUUID) {
        connectToRedis();
        Jedis jedis = pool.getResource();
        return jedis.hget(playerUUID.toString(), "wins");
    }

    public String getMostKillsUUID() {
        connectToRedis();
        Jedis jedis = pool.getResource();
        Set<String> allPlayers = jedis.keys("*");
        int highestKills = 0;
        String highestKillsPlayer = "";
        for (String playerString : allPlayers) {
            String value = jedis.hget(playerString, "kills");
            if (Integer.valueOf(value) > highestKills) {
                highestKills = Integer.valueOf(jedis.hget(playerString, "kills"));
                highestKillsPlayer = playerString;
            }
        }
        return highestKillsPlayer;
    }

    public String getMostDeathsUUID() {
        connectToRedis();
        Jedis jedis = pool.getResource();
        Set<String> allPlayers = jedis.keys("*");
        int highestDeaths = 0;
        String highestDeathsPlayer = "";
        for (String playerString : allPlayers) {
            String value = jedis.hget(playerString, "deaths");
            if (Integer.valueOf(value) > highestDeaths) {
                highestDeaths = Integer.valueOf(jedis.hget(playerString, "deaths"));
                highestDeathsPlayer = playerString;
            }
        }
        return highestDeathsPlayer;
    }

    public String getMostWinsUUID() {
        connectToRedis();
        Jedis jedis = pool.getResource();
        Set<String> allPlayers = jedis.keys("*");
        int highestWins = 0;
        String highestWinsPlayer = "";
        for (String playerString : allPlayers) {
            String value = jedis.hget(playerString, "wins");
            if (Integer.valueOf(value) > highestWins) {
                highestWins = Integer.valueOf(jedis.hget(playerString, "wins"));
                highestWinsPlayer = playerString;
            }
        }
        return highestWinsPlayer;
    }

    public void closePool() {
        pool.close();
    }

    public boolean isConnectedSuccessfully() {
        return connectedSuccessfully;
    }
}
