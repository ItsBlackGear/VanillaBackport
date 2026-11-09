package com.blackgear.vanillabackport.core.mixin.common.block_behavior;

import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourMixin {
    @Shadow
    public abstract Block getBlock();
    
    @Inject(method = "getSoundType", at = @At("RETURN"), cancellable = true)
    private void vb$updateSoundTypes(CallbackInfoReturnable<SoundType> cir) {
        Block block = this.getBlock();
        if (block == Blocks.ICE) {
            cir.setReturnValue(ModSoundTypes.ICE);
        } else if (block == Blocks.PACKED_ICE) {
            cir.setReturnValue(ModSoundTypes.PACKED_ICE);
        } else if (block == Blocks.BLUE_ICE) {
            cir.setReturnValue(ModSoundTypes.BLUE_ICE);
        } else if (block == Blocks.FROSTED_ICE) {
            cir.setReturnValue(ModSoundTypes.FROSTED_ICE);
        }
    }
}