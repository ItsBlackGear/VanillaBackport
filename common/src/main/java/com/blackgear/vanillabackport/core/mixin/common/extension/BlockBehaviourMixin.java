package com.blackgear.vanillabackport.core.mixin.common.extension;

import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {
    @Inject(method = "randomTick", at = @At("TAIL"))
    private void vb$onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockExtension.of(this).vb$randomTick(state, level, pos, random);
    }
    
    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void vb$isRandomlyTicking(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (BlockExtension.of(this).vb$isRandomlyTicking(state)) {
            cir.setReturnValue(true);
        }
    }
    
    @Inject(method = "onProjectileHit", at = @At("HEAD"))
    private void vb$onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile, CallbackInfo ci) {
        BlockExtension.of(this).vb$onProjectileHit(level, state, hit, projectile);
    }
}