package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimalMixin implements NeutralMob, WolfSoundVariantHolder {
    @Unique private WolfSoundVariantsModule module;
    @Shadow public abstract DyeColor getCollarColor();

    protected WolfMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public WolfSoundVariantsModule module() {
        if (this.module == null) {
            this.module = new WolfSoundVariantsModule(this.entityData);
        }

        return this.module;
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void vb$defineSynchedData(CallbackInfo ci) {
        this.module().defineSynchedData();
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.module().addAdditionalSaveData(tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.module().readAdditionalSaveData(tag);
    }

    @Override
    public WolfSoundVariant getSoundVariant() {
        return this.module().getSoundVariant();
    }

    @Override
    public void setSoundVariant(WolfSoundVariant variant) {
        this.module().setSoundVariant(variant);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.isAngry()) {
            cir.setReturnValue(this.getSoundVariant().growlSound().value());
        } else if (this.random.nextInt(3) == 0) {
            cir.setReturnValue(this.isTame() && this.getHealth() < 20.0F ? this.getSoundVariant().whineSound().value() : this.getSoundVariant().pantSound().value());
        } else {
            cir.setReturnValue(this.getSoundVariant().ambientSound().value());
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue(this.getSoundVariant().hurtSound().value());
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        cir.setReturnValue(this.getSoundVariant().deathSound().value());
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.module().finalizeSpawn(level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
        at = @At("RETURN"))
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Wolf> cir) {
        Wolf child = cir.getReturnValue();
        if (child != null && otherParent instanceof Wolf mate) {
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                child.setCollarColor(ColorUtils.getMixedColor(level, fatherColor, motherColor));
            }

            this.module().breedOffspring(child, ((Wolf)(Object)this));
        }
    }
}