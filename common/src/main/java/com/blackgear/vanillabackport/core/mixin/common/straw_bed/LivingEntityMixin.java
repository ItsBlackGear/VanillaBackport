package com.blackgear.vanillabackport.core.mixin.common.straw_bed;

import com.blackgear.vanillabackport.common.level.blocks.StrawBedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract Optional<BlockPos> getSleepingPos();
    @Shadow public abstract void clearSleepingPos();
    
    @Inject(method = "startSleeping", at = @At("HEAD"))
    private void vb$onStartSleeping(BlockPos pos, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        BlockState state = entity.level().getBlockState(pos);
        if (state.getBlock() instanceof StrawBedBlock) {
            entity.level().setBlock(pos, state.setValue(StrawBedBlock.OCCUPIED, true), 3);
        }
    }
    
    @Inject(method = "checkBedExists", at = @At("HEAD"), cancellable = true)
    private void vb$onCheckBedExists(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        boolean exists = this.getSleepingPos()
            .map(pos -> entity.level().getBlockState(pos).getBlock() instanceof StrawBedBlock)
            .orElse(false);
        
        if (exists) {
            cir.setReturnValue(true);
        }
    }
    
    @Inject(method = "getBedOrientation", at = @At("HEAD"), cancellable = true)
    private void vb$onGetBedOrientation(CallbackInfoReturnable<Direction> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        this.getSleepingPos().ifPresent(pos -> {
            BlockState state = entity.level().getBlockState(pos);
            if (state.getBlock() instanceof StrawBedBlock) {
                cir.setReturnValue(state.getValue(StrawBedBlock.FACING));
            }
        });
    }
    
    @Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
    private void vb$onStopSleeping(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        this.getSleepingPos().filter(entity.level()::hasChunkAt).ifPresent(pos -> {
            BlockState state = entity.level().getBlockState(pos);
            
            if (state.getBlock() instanceof StrawBedBlock strawBed) {
                Direction facing = state.getValue(StrawBedBlock.FACING);
                entity.level().setBlock(pos, state.setValue(StrawBedBlock.OCCUPIED, false), 3);
                
                Vec3 standUpPos = StrawBedBlock.findStandUpPosition(entity.getType(), entity.level(), pos, facing, entity.getYRot()).orElseGet(() -> {
                    BlockPos abovePos = pos.above();
                    return new Vec3(abovePos.getX() + 0.5, abovePos.getY() + 0.1, abovePos.getZ() + 0.5);
                });
                
                Vec3 lookVector = Vec3.atBottomCenterOf(pos).subtract(standUpPos).normalize();
                float yaw = (float) Mth.wrapDegrees(Mth.atan2(lookVector.z, lookVector.x) * (180.0 / Math.PI) - 90.0);
                
                entity.setPos(standUpPos.x, standUpPos.y, standUpPos.z);
                entity.setYRot(yaw);
                entity.setXRot(0.0F);
                
                strawBed.onStopSleeping(entity.level(), pos);
                Vec3 currentPos = entity.position();
                entity.setPose(Pose.STANDING);
                entity.setPos(currentPos.x, currentPos.y, currentPos.z);
                this.clearSleepingPos();
                
                ci.cancel();
            }
        });
    }
}