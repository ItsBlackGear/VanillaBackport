package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    @Invoker static void callAddMix(Potion potionEntry, Item potionIngredient, Potion potionResult) {
        throw new UnsupportedOperationException();
    }
}
