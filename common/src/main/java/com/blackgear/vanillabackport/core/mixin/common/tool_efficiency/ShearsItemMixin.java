package com.blackgear.vanillabackport.core.mixin.common.tool_efficiency;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
    private void customDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (state.is(ModBlockTags.WOOL_STAIRS) || state.is(ModBlockTags.WOOL_SLABS)) {
            cir.setReturnValue(5.0F);
        }
    }
}