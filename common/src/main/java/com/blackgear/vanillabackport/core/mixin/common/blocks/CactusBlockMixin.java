package com.blackgear.vanillabackport.core.mixin.common.blocks;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin extends Block {
    @Shadow @Final public static IntegerProperty AGE;

    @Shadow public abstract boolean canSurvive(BlockState state, LevelReader level, BlockPos pos);

    public CactusBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void vb$growCactusFlower(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockPos above = pos.above();
        if (VanillaBackport.COMMON_CONFIG.hasCactusFlowers.get() && level.isEmptyBlock(above)) {
            int height = 1;
            int age = state.getValue(AGE);

            while (level.getBlockState(pos.below(height)).is(CactusBlock.class.cast(this))) {
                if (height++ == 3 && age == 15) {
                    return;
                }
            }

            if (age == 8 && this.canSurvive(this.defaultBlockState(), level, above)) {
                double flowerChance = height >= 3 ? 0.25 : 0.1;
                if (random.nextDouble() <= flowerChance) {
                    level.setBlockAndUpdate(above, ModBlocks.CACTUS_FLOWER.get().defaultBlockState());
                }
            }
        }
    }

    @Inject(method = "canSurvive", at = @At("TAIL"), cancellable = true)
    private void vb$allowCactusPlacement(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState belowState = level.getBlockState(pos.below());
        boolean result = (belowState.is(CactusBlock.class.cast(this)) || belowState.is(ModBlockTags.SUPPORTS_CACTUS)) && !level.getBlockState(pos.above()).liquid();
        cir.setReturnValue(cir.getReturnValue() || result);
    }
}