package com.blackgear.vanillabackport.core.mixin.common.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LakeFeature.class)
public class LakeFeatureMixin {
    /**
     * Changes the origin offset from below(4) (i.e. offset(0, -4, 0)) to offset(-8, -4, -8),
     * centering the 16x16 lake generation area around the placement origin.
     */
    @ModifyVariable(
        method = "place",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/core/BlockPos;below(I)Lnet/minecraft/core/BlockPos;"
        ),
        ordinal = 0
    )
    private BlockPos vb$centerLakeOrigin(BlockPos blockPos) {
        return blockPos.offset(-8, 0, -8);
    }
}