package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnchantmentUtils {
    public static boolean isItemHasEnchantmentOfTag(ItemStack pStack, TagKey<Enchantment> pTag) {
        var enchantments = BuiltInRegistries.ENCHANTMENT.getTagOrEmpty(pTag);
        var itemStackEnchantments = EnchantmentHelper.getEnchantments(pStack);
        for (var foo : enchantments) {
            if(itemStackEnchantments.containsKey(foo.value())){
                return true;
            }
        }
        return false;
    }
}
