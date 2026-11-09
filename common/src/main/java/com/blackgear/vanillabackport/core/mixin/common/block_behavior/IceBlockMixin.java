package com.blackgear.vanillabackport.core.mixin.common.block_behavior;

import com.blackgear.vanillabackport.client.level.sound.AmbientIceBlockSoundsPlayer;
import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import com.blackgear.vanillabackport.common.level.blocks.BrittleIceBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(IceBlock.class)
public class IceBlockMixin implements BlockExtension {
    @WrapOperation(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/IceBlock;meltsInto()Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState vb$handleBrittleIceMelting(Operation<BlockState> original) {
        if ((IceBlock)(Object)this instanceof BrittleIceBlock) {
            return Blocks.AIR.defaultBlockState();
        }
        
        return original.call();
    }
    
    @Override
    public void vb$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        AmbientIceBlockSoundsPlayer.playAmbientIceSounds(level, pos, random);
    }
}