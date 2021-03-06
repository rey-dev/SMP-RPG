package me.rey.smp.utils;

import me.rey.smp.enchantments.CustomEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilItem {

    private static final Pattern enchantmentPattern = Pattern.compile("(.+) (M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$)");

    public static void updateEnchantments(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null) {
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

        outer:
        for (int i = 0; i < lore.size(); i++) {
            final String loreLine = lore.get(i);
            final Matcher matcher = enchantmentPattern.matcher(loreLine);
            if (!matcher.find()) {
                continue;
            }

            final String enchantmentName = matcher.group(1);
            for (final Enchantment enchantment : itemMeta.getEnchants().keySet()) {
                if (!(enchantment instanceof CustomEnchantment)) {
                    continue;
                }

                final CustomEnchantment customEnchantment = ((CustomEnchantment) enchantment);
                if (customEnchantment.getName().equals(ChatColor.stripColor(enchantmentName))) {
                    continue outer;
                }
            }

            lore.remove(i);
            i--;
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public static Collection<Enchantment> getItemEnchantments(final ItemStack itemStack) {
        if (itemStack == null || itemStack.hasItemMeta()) {
            return Collections.emptyList();
        }

        final Set<Enchantment> enchantments = new HashSet<>();
        if (itemStack.getItemMeta().hasLore()) {
            for (final String loreLine : itemStack.getItemMeta().getLore()) {
                final Matcher matcher = enchantmentPattern.matcher(loreLine);
                if (!matcher.find()) {
                    continue;
                }

                for (final Enchantment enchantment : Enchantment.values()) {
                    if (!(enchantment instanceof CustomEnchantment)) {
                        continue;
                    }

                    final CustomEnchantment customEnchantment = ((CustomEnchantment) enchantment);
                    if (customEnchantment.getName().equals(ChatColor.stripColor(matcher.group(1)))) {
                        enchantments.add(customEnchantment);
                        break;
                    }
                }
            }
        }

        enchantments.addAll(itemStack.getItemMeta().getEnchants().keySet());
        return enchantments;
    }

}
