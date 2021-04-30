package me.rey.smp.utils;

import me.rey.smp.enchantments.CustomEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilItem {

    public static void updateEnchantments(final ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) {
            return;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        final List<String> lore = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList<>();

        for (final Map.Entry<Enchantment, Integer> entry : itemMeta.getEnchants().entrySet()) {
            if (!(entry.getKey() instanceof CustomEnchantment)) {
                continue;
            }

            final CustomEnchantment enchantment = (CustomEnchantment) entry.getKey();
            final String enchantString = ChatColor.GRAY + enchantment.getName() + " " + UtilText.intToRomanNumeral(entry.getValue());

            boolean hasBeenUpdated = false;
            for (int i = 0; i < lore.size(); i++) {
                final String loreLine = lore.get(i);
                final Matcher matcher = Pattern.compile(enchantment.getName() + " (\\w+)").matcher(loreLine);

                if (!matcher.find()) {
                    continue;
                }

                if (UtilText.romanNumeralToInt(matcher.group(1)) < entry.getValue()) {
                    lore.set(i, enchantString);
                }

                hasBeenUpdated = true;
                break;
            }

            if (!hasBeenUpdated) {
                lore.add(0, enchantString);
            }

        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

}
