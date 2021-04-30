package me.rey.smp.enchantments.repo;

import me.rey.smp.enchantments.CustomEnchantment;
import me.rey.utils.utils.Text;
import org.bukkit.Sound;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;

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
    protected boolean canEnchantItemStack(final ItemStack item) {
        return EnchantmentTarget.TOOL.includes(item);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();

        if (!this.hasEnchant(handItem)) {
            return;
        }

        final Collection<ItemStack> drops = event.getBlock().getDrops(handItem);
        if (event.getBlock().getState() instanceof Container) {
            final Container container = (Container) event.getBlock().getState();
            drops.addAll(Arrays.asList(container.getInventory().getContents()));
            container.getInventory().clear();
        }

        for (final ItemStack toGive : drops) {
            if (toGive == null) {
                continue;
            }

            if (player.getInventory().firstEmpty() == -1) {

                event.getBlock().getWorld().dropItemNaturally(
                        event.getBlock().getLocation().add(0.5, 0.5, 0.5),
                        toGive
                );
                player.sendTitle("", Text.color("&c&lYour inventory is full!"), 0, 15, 0);
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 2F, 0.5F);

            } else {

                player.getInventory().addItem(toGive);

            }
        }

        event.setDropItems(false);
    }

}
