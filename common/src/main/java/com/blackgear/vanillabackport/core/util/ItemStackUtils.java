package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackUtils {
    public static void consume(ItemStack pStack, int pAmount, @Nullable LivingEntity pEntity) {
        if (pEntity == null || !(pEntity instanceof Player player && player.isCreative())) {
            pStack.shrink(pAmount);
        }
    }

    public static ItemStack consumeAndReturn(ItemStack pStack, int pAmount, @Nullable LivingEntity pEntity) {
        ItemStack itemstack = pStack.copyWithCount(pAmount);
        consume(pStack,pAmount, pEntity);
        return itemstack;
    }
}
