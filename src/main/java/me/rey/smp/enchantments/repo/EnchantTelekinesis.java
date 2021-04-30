package me.rey.smp.enchantments.repo;

import me.rey.smp.enchantments.CustomEnchantment;
import me.rey.utils.utils.Text;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantTelekinesis extends CustomEnchantment {

    public EnchantTelekinesis() {
        super("telekinesis", "Telekinesis", 1);
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(final Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(final ItemStack item) {
        return EnchantmentTarget.TOOL.includes(item);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!this.hasEnchant(handItem)) {
            return;
        }

        this.updateItemLore(handItem);
        for (final ItemStack toGive : event.getBlock().getDrops(handItem)) {
            if (player.getInventory().firstEmpty() == -1) {

                event.getBlock().getWorld().dropItemNaturally(
                        event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                        toGive
                );
                player.sendTitle("", Text.color("&c&lYour inventory is full!"), 2, 10, 2);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2F, 0.5F);

            } else {

                player.getInventory().addItem(toGive);

            }
        }

        final Block block = event.getBlock();
        event.setCancelled(true);
        block.getWorld().spawnParticle(
                Particle.ITEM_CRACK,
                block.getLocation().add(0.5, 0.5, 0.5),
                1,
                1,
                0.1,
                0.1,
                0.1,
                handItem
        );

        if (block instanceof Container) {
            final Container container = (Container) block;
            for (final ItemStack content : container.getInventory().getContents()) {
                block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), content);
            }
        }

        block.setType(Material.AIR);
    }

}
