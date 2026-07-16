package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.level.item.spear.SpearItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.item.enchantment.EnchantmentCategory$6")
public class EnchantmentCategoryMixin {
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void vb$allowSpearEnchantments(Item item, CallbackInfoReturnable<Boolean> cir) {
        if (item instanceof SpearItem) {
            cir.setReturnValue(true);
        }
    }
}