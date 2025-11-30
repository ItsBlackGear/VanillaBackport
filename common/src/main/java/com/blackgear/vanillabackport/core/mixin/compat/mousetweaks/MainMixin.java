package com.blackgear.vanillabackport.core.mixin.compat.mousetweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import yalter.mousetweaks.Main;

@Pseudo @Mixin(Main.class)
public class MainMixin {
    @WrapOperation(
        method = "onMouseScrolled",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
            ordinal = 0
        )
    )
    private static boolean vb$onMouseScrolled(ItemStack instance, Operation<Boolean> original) {
        return instance.getItem() instanceof BundleItem || original.call(instance);
    }
}