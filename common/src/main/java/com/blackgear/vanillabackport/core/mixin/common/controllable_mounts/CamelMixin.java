package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camel.class)
public class CamelMixin extends AbstractHorse {
    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$getCamelLookControl(EntityType<? extends AbstractHorse> entityType, Level level, CallbackInfo ci) {
        Camel camel = (Camel)(Object)this;
        this.lookControl = new LookControl(camel) {
            @Override public void tick() {
                if (!camel.hasControllingPassenger()) {
                    super.tick();
                }
            }
        };
    }
    
    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return super.getControllingPassenger();
    }
}