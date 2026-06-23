package com.blackgear.vanillabackport.core.mixin.common.extension;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends MobMixin {
    @Shadow public abstract boolean isTame();

    protected TamableAnimalMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "setTame", at = @At("TAIL"))
    private void vb$onSetTame(boolean tamed, CallbackInfo ci) {
        if (!tamed) this.applyTamingSideEffects();
    }

    @Inject(method = "tame", at = @At("TAIL"))
    private void vb$onTame(Player player, CallbackInfo ci) {
        this.applyTamingSideEffects();
    }

    @Unique
    protected void applyTamingSideEffects() {
    }
}