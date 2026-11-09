package com.blackgear.vanillabackport.core.mixin.common.frozen_mobs;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {
    @Shadow @Final protected FlowingFluid fluid;
    
    @Inject(method = "shouldSpreadLiquid", at = @At("HEAD"), cancellable = true)
    private void vb$shouldSpreadLiquid(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (this.fluid == Fluids.WATER) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos neighbourPos = pos.relative(direction.getOpposite());
                if (level.getBlockState(neighbourPos).is(Blocks.BLUE_ICE)) {
                    level.setBlockAndUpdate(pos, ModBlocks.BRITTLE_ICE.get().defaultBlockState());
                    cir.setReturnValue(false);
                }
            }
        }
    }
}