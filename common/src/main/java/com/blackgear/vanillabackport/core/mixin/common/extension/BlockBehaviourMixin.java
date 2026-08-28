package com.blackgear.vanillabackport.core.mixin.common.extension;

import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void vb$onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockExtension.of(this).vb$randomTick(state, level, pos, random);
    }
    
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void vb$isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (BlockExtension.of(this).vb$isRandomlyTicking(state)) {
            cir.setReturnValue(true);
        }
    }
}