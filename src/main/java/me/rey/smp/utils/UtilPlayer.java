package me.rey.smp.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class UtilPlayer {

    public static void addEffect(final Player player, final PotionEffect effect) {
        // TODO: Add custom efecto repo
        player.addPotionEffect(effect);
    }

}
