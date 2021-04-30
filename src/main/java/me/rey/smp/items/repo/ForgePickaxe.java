package me.rey.smp.items.repo;

import me.rey.smp.items.CustomItem;
import me.rey.smp.utils.UtilPlayer;
import me.rey.utils.events.UpdateEvent;
import me.rey.utils.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class ForgePickaxe extends CustomItem {

    private static final PotionEffect hasteEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, 4, 2);

    public ForgePickaxe() {
        super(
                "FP",
                new Item(Material.DIAMOND_PICKAXE).setName("&9Forge Pickaxe").setGlow(true),
                Arrays.asList("Gain permanent Haste III while holding", "this pickaxe.")
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUpdate(final UpdateEvent e) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final PlayerInventory playerInventory = player.getInventory();

            if (!ForgePickaxe.this.doesItemMatch(playerInventory.getItemInMainHand())
                    && !ForgePickaxe.this.doesItemMatch(playerInventory.getItemInOffHand())) {
                continue;
            }

            UtilPlayer.addEffect(player, hasteEffect);
        }
    }

}
