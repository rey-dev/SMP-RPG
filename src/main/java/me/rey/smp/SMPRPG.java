package me.rey.smp;

import me.rey.smp.enchantments.CustomEnchantment;
import me.rey.smp.enchantments.SMPEnchants;
import me.rey.smp.items.CustomItem;
import me.rey.smp.items.repo.ForgePickaxe;
import me.rey.smp.items.repo.MagicalStickOfBonking;
import me.rey.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public final class SMPRPG extends JavaPlugin {

    private final Map<String, CustomItem> registeredCustomItems = new HashMap<>();
    private final List<CustomEnchantment> registeredCustomEnchantments = new ArrayList<>();

    static SMPRPG instance;

    public static SMPRPG getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ServerUtils.setupAPI(this);
        Objects.requireNonNull(this.getCommand("cigive")).setExecutor(new MainCommand());

        this.registerCustomItems(
                new MagicalStickOfBonking(),
                new ForgePickaxe()
        );

        this.registerCustomEnchantment(
                SMPEnchants.TELEKINESIS
        );

        for (final CustomItem customItem : this.getRegisteredCustomItems().values()) {
            Bukkit.getPluginManager().registerEvents(customItem, this);
        }

        for (final CustomEnchantment customEnchantment : this.registeredCustomEnchantments) {
            Bukkit.getPluginManager().registerEvents(customEnchantment, this);
        }

    }

    @Override
    public void onDisable() {

    }

    public Map<String, CustomItem> getRegisteredCustomItems() {
        return this.registeredCustomItems;
    }

    private void registerCustomItems(final CustomItem... items) {
        this.registeredCustomItems.clear();
        for (final CustomItem item : items) {
            if (this.registeredCustomItems.containsKey(item.getId())) {
                throw new RuntimeException("CustomItems cannot have identical IDs");
            }
            this.registeredCustomItems.put(item.getId(), item);
        }
    }

    private void registerCustomEnchantment(final CustomEnchantment... customEnchantments) {
        try {
            final Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);

            final Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            byKeyField.setAccessible(true);
            final Map<NamespacedKey, Enchantment> byKey = (HashMap) byKeyField.get(null);

            final Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            final Map<String, Enchantment> byName = (HashMap) byNameField.get(null);

            for (final CustomEnchantment customEnchantment : customEnchantments) {
                if (byKey.containsKey(customEnchantment.getKey()) || byName.containsKey(customEnchantment.getName())) {
                    byKey.remove(customEnchantment.getKey());
                    byName.remove(customEnchantment.getName());
                }

                Enchantment.registerEnchantment(customEnchantment);
                Bukkit.getLogger().log(Level.FINE, "Successfully registered enchantment [" + customEnchantment.getName() + "]");
                this.registeredCustomEnchantments.add(customEnchantment);
            }

            acceptingNew.setAccessible(false);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.SEVERE, "Could not register custom enchantments!");
        }
    }

    public Optional<CustomItem> getCustomItemFromId(final String id) {
        return Optional.ofNullable(this.getRegisteredCustomItems().get(id));
    }

}
