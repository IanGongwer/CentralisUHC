package com.iangongwer.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;

public class HeartUtil {

    public static void showHealth(Player player, Scoreboard scoreboard, String title) {
        title = "uhc.centralis.cc";
        BPlayerBoard scoreboardNew = new BPlayerBoard(player, scoreboard, title);
        if (scoreboardNew.getScoreboard().getObjective(DisplaySlot.BELOW_NAME) == null) {
            Objective health = scoreboardNew.getScoreboard().registerNewObjective("showhealth",
                    Criterias.HEALTH);
            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
            health.setDisplayName(ChatColor.DARK_RED + "❤");
        } else {
            Objective getHealth = scoreboardNew.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
            getHealth.setDisplaySlot(DisplaySlot.BELOW_NAME);
            getHealth.setDisplayName(ChatColor.DARK_RED + "❤");
        }
        if (scoreboardNew.getScoreboard().getObjective(DisplaySlot.PLAYER_LIST) == null) {
            Objective health2 = scoreboardNew.getScoreboard().registerNewObjective("showhealth2",
                    Criterias.HEALTH);
            health2.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            health2.setDisplayName(ChatColor.DARK_RED + "❤");
        } else {
            Objective getHealth2 = scoreboardNew.getScoreboard().getObjective(DisplaySlot.PLAYER_LIST);
            getHealth2.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            getHealth2.setDisplayName(ChatColor.DARK_RED + "❤");
        }
        scoreboardNew = null;
    }

}