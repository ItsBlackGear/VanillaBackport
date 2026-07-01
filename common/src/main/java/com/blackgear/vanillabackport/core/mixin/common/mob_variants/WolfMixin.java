package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariants;
import com.blackgear.vanillabackport.common.integrations.compat.BackportedWolvesConversion;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfVariants;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements EntityDataHolder, MobBehaviorAccess, WolfSoundVariantHolder, VariantDataHolder<WolfVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    @Shadow public abstract DyeColor getCollarColor();

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void vb$defineSynchedData() {
        this.entityData.define(DATA_SOUND_VARIANT_ID, VariantUtils.getDefaultID(WolfSoundVariants.REGISTRIES, WolfSoundVariants.CLASSIC));
        this.entityData.define(DATA_VARIANT_ID, "minecraft:pale");
    }

    @Override
    public Optional<WolfVariant> getVariantData() {
        return VariantUtils.getOrDefault(WolfVariants.REGISTRIES, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void setVariantData(WolfVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(WolfVariants.REGISTRIES, variant));
    }

    @Override
    public WolfSoundVariant vb$getSoundVariant() {
        return VariantUtils.getVariant(WolfSoundVariants.REGISTRIES, this.entityData.get(DATA_SOUND_VARIANT_ID));
    }

    @Override
    public void vb$setSoundVariant(WolfSoundVariant variant) {
        this.entityData.set(DATA_SOUND_VARIANT_ID, VariantUtils.getID(WolfSoundVariants.REGISTRIES, variant));
    }

    @Override
    public void vb$addAdditionalSaveData(CompoundTag tag) {
        VariantUtils.addVariantSaveData(this, tag, WolfVariants.REGISTRIES);
        tag.putString("sound_variant", WolfSoundVariants.REGISTRIES.getKey(this.vb$getSoundVariant()).toString());
    }

    @Override
    public void vb$readAdditionalSaveData(CompoundTag tag) {
        BackportedWolvesConversion.migrateWolfVariant(this, tag, WolfVariants.REGISTRIES);
        VariantUtils.readVariantSaveData(this, tag, WolfVariants.REGISTRIES);
        WolfSoundVariant soundVariant = WolfSoundVariants.REGISTRIES.get(ResourceLocation.tryParse(tag.getString("sound_variant")));
        if (soundVariant != null) this.vb$setSoundVariant(soundVariant);
    }

    @Override
    public void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        this.vb$setSoundVariant(WolfSoundVariants.REGISTRIES.getRandomElement(level.getRandom()));
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), WolfVariants.REGISTRIES, VariantSpawner.WOLF_VARIANTS)
            .ifPresent(this::setVariantData);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(DamageSource damageSource, CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }
    
    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Wolf> cir) {
        Wolf baby = cir.getReturnValue();
        if (baby != null && otherParent instanceof Wolf mate) {
            VariantDataHolder.trySetOffspringVariant(baby, this, mate);
            
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                baby.setCollarColor(ColorUtils.getMixedColor(level, fatherColor, motherColor));
            }
            
            WolfSoundVariantHolder.of(baby).vb$setSoundVariant(WolfSoundVariants.REGISTRIES.getRandomElement(this.getRandom()));
        }
    }
}