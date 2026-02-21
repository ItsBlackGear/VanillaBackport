package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class DyeableAnimalArmorItem extends AnimalArmorItem implements DyeableLeatherItem {
    public DyeableAnimalArmorItem(AnimalMaterial material, BodyType type, int durability, Properties properties) {
        super(material, type, durability, properties);
    }

    public static int getColorOrDefault(ItemStack stack, int fallback) {
        if (stack.getItem() instanceof DyeableLeatherItem item) {
            return item.getColor(stack);
        }
        return fallback;
    }
}
