package com.blackgear.vanillabackport.core.mixin.common.block_behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {
    @Shadow @Final public static IntegerProperty LAYERS;
    
    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void vb$canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // yes, I know there is a tag to override, however that's on the second check
        // packed ice is already added into the minecraft:cannot_support_snow_layer tag and last i checked we cannot remove entries from tags
        // so even if added it into the minecraft:support_override_snow_layer tag it'd be ignored cuz it was already marked out by the first check
        // so i'm adding this here...
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.is(Blocks.PACKED_ICE)) {
            cir.setReturnValue(Block.isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP) || belowState.is((Block)(Object)this) && belowState.getValue(LAYERS) == 8);
        }
    }
}