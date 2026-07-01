package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariants;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfDataVariants;
import com.blackgear.vanillabackport.core.mixin.common.access.WolfAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Optional;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements WolfSoundVariantHolder, VariantDataHolder<WolfDataVariant>, EntityDataHolder, MobBehaviorAccess {
    @Unique private static EntityDataAccessor<String> DATA_SOUND_VARIANT_ID;
    @Unique private static EntityDataAccessor<String> DATA_VARIANT_ID;
    @Shadow public abstract DyeColor getCollarColor();
    
    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void vb$registerAccessor(CallbackInfo ci) {
        DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
        DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    }
    
    @Override
    public void vb$defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SOUND_VARIANT_ID, VariantUtils.getDefaultID(WolfSoundVariants.REGISTRIES, WolfSoundVariants.CLASSIC));
        builder.define(DATA_VARIANT_ID, "minecraft:pale");
    }
    
    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, WolfDataVariants.REGISTRIES);
        tag.putString("sound_variant", WolfSoundVariants.REGISTRIES.getKey(this.getSoundVariant()).toString());
    }
    
    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        VariantUtils.readVariantSaveData(this, tag, WolfDataVariants.REGISTRIES);
        WolfSoundVariant soundVariant = WolfSoundVariants.REGISTRIES.get(ResourceLocation.tryParse(tag.getString("sound_variant")));
        if (soundVariant != null) this.setSoundVariant(soundVariant);
    }
    
    @Override
    public WolfSoundVariant getSoundVariant() {
        return VariantUtils.getVariant(WolfSoundVariants.REGISTRIES, this.entityData.get(DATA_SOUND_VARIANT_ID));
    }
    
    @Override
    public void setSoundVariant(WolfSoundVariant variant) {
        this.entityData.set(DATA_SOUND_VARIANT_ID, VariantUtils.getID(WolfSoundVariants.REGISTRIES, variant));
    }
    
    @Override
    public Optional<WolfDataVariant> getVariantData() {
        return VariantUtils.getOrDefault(WolfDataVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }
    
    @Override
    public void setVariantData(WolfDataVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(WolfDataVariants.REGISTRIES, variant));
    }
    
    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
        this.setSoundVariant(WolfSoundVariants.REGISTRIES.getRandomElement(level.getRandom()));
        
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
            
            WolfSoundVariantHolder.of(child).setSoundVariant(WolfSoundVariants.REGISTRIES.getRandomElement(this.getRandom()));
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }
}