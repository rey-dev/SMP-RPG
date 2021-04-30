package me.rey.smp.listeners;

import me.rey.smp.utils.UtilItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        final List<ItemStack> toUpdate = new ArrayList<>(Arrays.asList(event.getInventory().getContents()));
        this.addPlayerContents(toUpdate, event.getPlayer());

        for (final ItemStack content : toUpdate) {
            UtilItem.updateEnchantments(content);
        }

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerLogin(final PlayerJoinEvent event) {
        final List<ItemStack> toUpdate = new ArrayList<>();
        this.addPlayerContents(toUpdate, event.getPlayer());

        for (final ItemStack content : toUpdate) {
            UtilItem.updateEnchantments(content);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInventory(final InventoryClickEvent event) {
        final List<ItemStack> toUpdate = new ArrayList<>();
        this.addPlayerContents(toUpdate, event.getWhoClicked());

        for (final ItemStack content : toUpdate) {
            UtilItem.updateEnchantments(content);
        }
    }

    private void addPlayerContents(final List<ItemStack> toUpdate, final HumanEntity player) {
        toUpdate.addAll(Arrays.asList(player.getInventory().getContents()));
        toUpdate.addAll(Arrays.asList(player.getInventory().getArmorContents()));
        toUpdate.addAll(Arrays.asList(player.getInventory().getExtraContents()));
        toUpdate.addAll(Arrays.asList(player.getInventory().getStorageContents()));


    }

}
