package com.blackgear.vanillabackport.core.mixin.common.creaking_heart;

import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final public Level level;
    
    @Inject(
        method = "finalizeExplosion",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
            ordinal = 0
        )
    )
    private void vb$removeProtectorPostExplosion(boolean spawnParticles, CallbackInfo ci, @Local(ordinal = 0) BlockPos pos, @Local BlockState state) {
        if (state.getBlock() instanceof CreakingHeartBlock heart) {
            heart.onExplosionHit(this.level, pos, state, (Explosion) (Object) this);
        }
    }
}