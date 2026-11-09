package com.blackgear.vanillabackport.core.mixin.common.worldgen.ice_caves;

import com.blackgear.vanillabackport.common.api.extensions.worldgen.PosAwareRuleTest;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(OreFeature.class)
public class OreFeatureMixin {
    @Shadow
    protected static boolean shouldSkipAirCheck(RandomSource random, float chance) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
    
    @Inject(method = "canPlaceOre", at = @At("HEAD"), cancellable = true)
    private static void vb$canPlaceOre(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random, OreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (PosAwareRuleTest.of(targetState.target).test(state, pos, random)) {
            cir.setReturnValue(shouldSkipAirCheck(random, config.discardChanceOnAirExposure) || !Feature.isAdjacentToAir(adjacentStateAccessor, pos));
        }
    }
}