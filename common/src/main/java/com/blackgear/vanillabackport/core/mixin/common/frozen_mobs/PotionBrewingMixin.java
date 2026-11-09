package com.blackgear.vanillabackport.core.mixin.common.frozen_mobs;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.items.ModPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
    @Inject(method = "addVanillaMixes", at = @At("TAIL"))
    private static void vb$addVanillaMixes(PotionBrewing.Builder builder, CallbackInfo ci) {
        builder.addStartMix(ModItems.ICE_BALL.get(), ModPotions.FREEZING);
        builder.addMix(ModPotions.FREEZING, Items.REDSTONE, ModPotions.LONG_FREEZING);
    }
}