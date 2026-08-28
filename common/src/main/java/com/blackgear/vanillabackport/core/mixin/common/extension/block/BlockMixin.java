package com.blackgear.vanillabackport.core.mixin.common.extension.block;

import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {
    @Inject(method = "animateTick", at = @At("HEAD"))
    public void vb$onAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        this.vb$animateTick(state, level, pos, random);
    }
    
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void vb$isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (BlockExtension.of(this).vb$isRandomlyTicking(state)) {
            cir.setReturnValue(true);
        }
    }
}