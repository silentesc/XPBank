package de.silentesc.xpbank.utils;

import org.bukkit.entity.Player;

public class XpUtils {
    public static void addXp(Player player, int xp) {
        player.giveExp(xp);
    }

    public static void removeXp(Player player, int xp) {
        int newXp = player.getTotalExperience() - xp;
        setXp(player, newXp);
    }

    public static void setXp(Player player, int xp) {
        resetXp(player);
        player.giveExp(xp);
    }

    public static void resetXp(Player player) {
        player.setLevel(0);
        player.setExp(0);
        player.setTotalExperience(0);
    }
}
