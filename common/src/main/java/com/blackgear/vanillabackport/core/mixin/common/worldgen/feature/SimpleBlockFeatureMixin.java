package com.blackgear.vanillabackport.core.mixin.common.worldgen.feature;

import com.blackgear.vanillabackport.common.level.blocks.EyeblossomBlock;
import com.blackgear.vanillabackport.common.level.blocks.MossyCarpetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleBlockFeature.class)
public class SimpleBlockFeatureMixin {
    @Inject(
        method = "place",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onPlace(FeaturePlaceContext<SimpleBlockConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockState state = config.toPlace().getState(context.random(), origin);
        Block block = state.getBlock();
        if (state.canSurvive(level, origin)) {
            if (block instanceof MossyCarpetBlock) {
                MossyCarpetBlock.placeAt(level, origin, level.getRandom(), Block.UPDATE_CLIENTS);
                cir.setReturnValue(true);
            }

            if (block instanceof EyeblossomBlock) {
                level.setBlock(origin, state, Block.UPDATE_CLIENTS);
                level.scheduleTick(origin, level.getBlockState(origin).getBlock(), 1);
                cir.setReturnValue(true);
            }
        }
    }
}