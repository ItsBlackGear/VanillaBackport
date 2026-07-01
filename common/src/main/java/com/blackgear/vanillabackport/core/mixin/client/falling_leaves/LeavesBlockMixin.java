package com.blackgear.vanillabackport.core.mixin.client.falling_leaves;

import com.blackgear.vanillabackport.client.api.modules.falling_leaves.FallingLeavesModule;
import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LeavesBlock.class)
@Environment(EnvType.CLIENT)
public class LeavesBlockMixin implements BlockExtension {
    @Unique private final FallingLeavesModule module = new FallingLeavesModule();
    
    @Override
    public void vb$AnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        this.module.makeFallingLeavesParticles(level, pos, random, level.getBlockState(pos.below()), pos.below());
    }
}