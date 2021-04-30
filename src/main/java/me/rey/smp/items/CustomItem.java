package me.rey.smp.items;

import me.rey.smp.SMPRPG;
import me.rey.utils.items.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class CustomItem implements Listener {

    private final Item item;
    private final String id;

    public CustomItem(final String id, final Item item) {
        this.id = id;
        this.item = item;
    }

    public String getId() {
        return this.id;
    }

    public ItemStack get() {
        final ItemStack itemToGive = this.item.get();
        final ItemMeta itemMeta = itemToGive.getItemMeta();
        final PersistentDataContainer dataContainer = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        final NamespacedKey namespacedKey = new NamespacedKey(SMPRPG.getInstance(), "customitem");
        dataContainer.set(namespacedKey, PersistentDataType.STRING, this.getId());
        itemToGive.setItemMeta(itemMeta);
        return itemToGive;
    }

    protected boolean isPlayerHolding(final Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        return this.doesItemMatch(playerInventory.getItemInMainHand()) || this.doesItemMatch(playerInventory.getItemInOffHand());
    }

    protected boolean doesItemMatch(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return false;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer dataContainer = Objects.requireNonNull(itemMeta).getPersistentDataContainer();
        final NamespacedKey namespacedKey = new NamespacedKey(SMPRPG.getInstance(), "customitem");
        if (!dataContainer.getKeys().contains(namespacedKey)) {
            return false;
        }

        try {
            return Optional.ofNullable(dataContainer.get(namespacedKey, PersistentDataType.STRING)).orElse("").equals(this.getId());
        } catch (final NullPointerException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Optional<CustomItem> getAsCustomItem(final ItemStack itemStack) {
        for (final Map.Entry<String, CustomItem> item : SMPRPG.getInstance().getRegisteredCustomItems().entrySet()) {
            if (item.getValue().doesItemMatch(itemStack)) {
                return Optional.of(item.getValue());
            }
        }

        return Optional.empty();
    }

}