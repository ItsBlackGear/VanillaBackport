package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.SoundVariantHolder;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariants;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfDataVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfDataVariants;
import com.blackgear.vanillabackport.common.registries.entities.ModSyncedEntityData;
import com.blackgear.vanillabackport.core.mixin.common.access.WolfAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements SoundVariantHolder<WolfSoundVariant>, VariantDataHolder<WolfDataVariant>, EntityDataHolder, MobBehaviorAccess {
    @Shadow public abstract DyeColor getCollarColor();
    
    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, WolfDataVariants.REGISTRIES);
        VariantUtils.addSoundVariantSaveData(this, tag, WolfSoundVariants.REGISTRIES);
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, WolfDataVariants.REGISTRIES);
        VariantUtils.readSoundVariantSaveData(this, tag, WolfSoundVariants.REGISTRIES);
    }
    
    @Override
    public WolfSoundVariant vb$getSoundVariant() {
        return VariantUtils.getVariant(this, WolfSoundVariants.REGISTRIES, ModSyncedEntityData.WOLF_SOUND_VARIANTS);
    }
    
    @Override
    public void vb$setSoundVariant(WolfSoundVariant variant) {
        VariantUtils.setVariant(this, variant, WolfSoundVariants.REGISTRIES, ModSyncedEntityData.WOLF_SOUND_VARIANTS);
    }
    
    @Override
    public Optional<WolfDataVariant> getVariantData() {
        return Optional.ofNullable(VariantUtils.getVariant(this, WolfDataVariants.REGISTRIES, ModSyncedEntityData.WOLF_VARIANTS));
    }
    
    @Override
    public void setVariantData(WolfDataVariant variant) {
        VariantUtils.setVariant(this, variant, WolfDataVariants.REGISTRIES, ModSyncedEntityData.WOLF_VARIANTS);
    }
    
    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & SoundVariantHolder<WolfSoundVariant>) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & SoundVariantHolder<WolfSoundVariant>) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & SoundVariantHolder<WolfSoundVariant>) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
        this.vb$setSoundVariant(WolfSoundVariants.REGISTRIES.getRandomElement(level.getRandom()));
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), WolfDataVariants.REGISTRIES)
            .ifPresent(this::setVariantData);
    }
    
    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Wolf> cir) {
        Wolf child = cir.getReturnValue();
        if (child != null && otherParent instanceof Wolf mate) {
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                ((WolfAccessor) child).callSetCollarColor(ColorUtils.getMixedColor(level, fatherColor, motherColor));
            }
            
            SoundVariantHolder.trySetOffspringVariant(child, this, WolfSoundVariants.REGISTRIES);
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }
}