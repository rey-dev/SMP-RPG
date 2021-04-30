package me.rey.smp.enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment extends Enchantment implements Listener {

    private final int maxLevel;
    private final String name;

    public CustomEnchantment(final String namespacedKey, final String name, final int maxLevel) {
        super(NamespacedKey.minecraft(namespacedKey));
        this.name = name;
        this.maxLevel = maxLevel;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean canEnchantItem(final ItemStack item) {
        return item.getType().equals(Material.ENCHANTED_BOOK) || this.canEnchantItemStack(item);
    }

    protected abstract boolean canEnchantItemStack(ItemStack itemStack);

    protected boolean hasEnchant(final ItemStack itemStack) {
        return itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchant(SMPEnchants.TELEKINESIS);
    }

}
