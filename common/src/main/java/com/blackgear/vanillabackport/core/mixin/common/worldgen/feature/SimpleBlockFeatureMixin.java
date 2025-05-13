package com.blackgear.vanillabackport.core.mixin.common.worldgen.feature;

import com.blackgear.vanillabackport.common.level.blocks.EyeblossomBlock;
import com.blackgear.vanillabackport.common.level.blocks.MossyCarpetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
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
        if (state.canSurvive(level, origin)) {
            if (state.getBlock() instanceof MossyCarpetBlock) {
                MossyCarpetBlock.placeAt(level, origin, level.getRandom(), 2);
                cir.setReturnValue(true);
            }

            if (state.getBlock() instanceof EyeblossomBlock) {
                level.scheduleTick(origin, level.getBlockState(origin).getBlock(), 1);
                level.setBlock(origin, state, 2);
                cir.setReturnValue(true);
            }
        }
    }
}