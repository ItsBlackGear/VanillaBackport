package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.TravelAwareEntity;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements TravelAwareEntity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public void postTravelInFluid() {
        if (!VanillaBackport.COMMON_CONFIG.canMountsFloatWhileRidden.get()) return;
        
        if (this.isInWater() && this.getType().is(ModEntityTypeTags.CAN_FLOAT_WHILE_RIDDEN)) {
            if (this.isVehicle() && this.getFluidHeight(FluidTags.WATER) > this.getFluidJumpThreshold()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04F, 0.0));
            }
        }
    }
}