package com.blackgear.vanillabackport.core.mixin.common.blocks;

import com.blackgear.vanillabackport.common.api.FallingLeavesModule;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class) @Environment(EnvType.CLIENT)
public class LeavesBlockMixin {
    @Unique private final FallingLeavesModule module = new FallingLeavesModule();

    @Inject(method = "animateTick", at = @At("HEAD"))
    public void vb$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        this.module.makeFallingLeavesParticles(level, pos, random, level.getBlockState(pos.below()), pos.below());
    }
}