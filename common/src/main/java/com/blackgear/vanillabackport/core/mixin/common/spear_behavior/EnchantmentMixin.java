package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.level.item.spear.SpearItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Shadow @Final public EnchantmentCategory category;
    
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void vb$preventSweepingEdgeOnSpear(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (this.category != EnchantmentCategory.WEAPON) return;
        
        if (!(stack.getItem() instanceof SpearItem)) return;
        
        Enchantment enchantment = (Enchantment) (Object) this;
        if (enchantment == Enchantments.SWEEPING_EDGE) {
            cir.setReturnValue(false);
        }
    }
}