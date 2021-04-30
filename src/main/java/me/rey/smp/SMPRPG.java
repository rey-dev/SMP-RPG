package me.rey.smp;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SMPRPG extends JavaPlugin {

    private final Map<String, CustomItem> registeredCustomItems = new HashMap<>();

    static SMPRPG instance;

    public static SMPRPG getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(this.getCommand("cigive")).setExecutor(new MainCommand());

        registerCustomItems(
              new MagicalStickOfBonking()
        );

        for (CustomItem customItem : getRegisteredCustomItems().values()) {
            Bukkit.getPluginManager().registerEvents(customItem, this);
        }
    }

    @Override
    public void onDisable() {

    }

    public Map<String, CustomItem> getRegisteredCustomItems() {
        return registeredCustomItems;
    }

    private void registerCustomItems(final CustomItem... items) {
        registeredCustomItems.clear();
        for (CustomItem item : items) {
            if (registeredCustomItems.containsKey(item.getId())) {
                throw new RuntimeException("CustomItems cannot have identical IDs");
            }
            registeredCustomItems.put(item.getId(), item);
        }
    }

    public Optional<CustomItem> getCustomItemFromId(String id) {
        return Optional.ofNullable(getRegisteredCustomItems().get(id));
    }

    public Optional<CustomItem> getAsCustomItem(ItemStack itemStack) {
        for (Map.Entry<String, CustomItem> item : getRegisteredCustomItems().entrySet()) {
            if (item.getValue().doesItemMatch(itemStack)) {
                return Optional.of(item.getValue());
            }
        }

        return Optional.empty();
    }

}
