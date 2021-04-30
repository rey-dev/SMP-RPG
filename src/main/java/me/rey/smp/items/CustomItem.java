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

import java.util.*;

public abstract class CustomItem implements Listener {

    private static final ArrayList<String> DEFAULT_DESCRIPTION = new ArrayList<>(Arrays.asList(
            "&7This is the default item description!"
    ));

    private final Item item;
    private final String id;
    private final Collection<String> description;

    public CustomItem(final String id, final Item item, final Collection<String> description) {
        this.id = id;
        this.item = item;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public ItemStack get() {
        final List<String> lore = new ArrayList<>(this.description == null ? DEFAULT_DESCRIPTION : this.description);
        lore.add(0, "&r");
        lore.add(1, "&8&m                                                 &m");
        lore.add(2, "&r");
        lore.add(3, "&f" + this.item.getName());
        this.item.setLore(lore);

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