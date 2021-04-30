package me.rey.smp;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.rey.utils.items.Item;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public abstract class CustomItem implements Listener {

    private final Item item;
    private final String id;

    public CustomItem(String id, Item item) {
        this.id = id;
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public ItemStack get() {
        ItemStack itemToGive = item.get();
        NBTItem nbt = new NBTItem(itemToGive);
        nbt.setString("customitem", this.getId());
        nbt.applyNBT(itemToGive);
        return itemToGive;
    }

    protected boolean doesItemMatch(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
            return false;
        }

        NBTItem nbt = new NBTItem(itemStack);
        if (!nbt.getKeys().contains("customitem")) {
            return false;
        }

        return nbt.getString("customitem").equals(this.getId());
    }

}