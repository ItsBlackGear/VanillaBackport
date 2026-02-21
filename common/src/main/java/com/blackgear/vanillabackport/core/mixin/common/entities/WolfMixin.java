package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.variant.VariantSpawner;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.wolf.BackportedWolvesConversion;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariants;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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
public abstract class WolfMixin extends TamableAnimalMixin implements NeutralMob, WolfSoundVariantHolder, VariantDataHolder<WolfVariant> {
    @Unique private static final EntityDataAccessor<String> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    @Unique private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    @Shadow public abstract DyeColor getCollarColor();

    protected WolfMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
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
                child.setCollarColor(ColorUtils.getMixedColor(level, fatherColor, motherColor));
            }

            WolfSoundVariantHolder.of(child).vb$setSoundVariant(ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getRandomElement(this.getRandom()));
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_SOUND_VARIANT_ID, VariantUtils.getDefaultID(ModBuiltinRegistries.WOLF_SOUND_VARIANTS, WolfSoundVariants.CLASSIC));
        this.entityData.define(DATA_VARIANT_ID, "minecraft:pale");
    }

    @Override
    public Optional<WolfVariant> getVariantData() {
        return VariantUtils.getOrDefault(ModBuiltinRegistries.WOLF_VARIANTS, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void setVariantData(WolfVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(ModBuiltinRegistries.WOLF_VARIANTS, variant));
    }

    @Override
    public WolfSoundVariant vb$getSoundVariant() {
        return VariantUtils.getVariant(ModBuiltinRegistries.WOLF_SOUND_VARIANTS, this.entityData.get(DATA_SOUND_VARIANT_ID));
    }

    @Override
    public void vb$setSoundVariant(WolfSoundVariant variant) {
        this.entityData.set(DATA_SOUND_VARIANT_ID, VariantUtils.getID(ModBuiltinRegistries.WOLF_SOUND_VARIANTS, variant));
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, ModBuiltinRegistries.WOLF_VARIANTS);
        tag.putString("sound_variant", ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getKey(this.vb$getSoundVariant()).toString());
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        BackportedWolvesConversion.migrateWolfVariant(this, tag, ModBuiltinRegistries.WOLF_VARIANTS);

        VariantUtils.readVariantSaveData(this, tag, ModBuiltinRegistries.WOLF_VARIANTS);
        WolfSoundVariant soundVariant = ModBuiltinRegistries.WOLF_SOUND_VARIANTS.get(ResourceLocation.tryParse(tag.getString("sound_variant")));
        if (soundVariant != null) this.vb$setSoundVariant(soundVariant);
    }

    @Override
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.vb$setSoundVariant(ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getRandomElement(level.getRandom()));
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.WOLF_VARIANTS, VariantSpawner.WOLF_VARIANTS)
            .ifPresent(this::setVariantData);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void vb$damageWolfArmor(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Wolf wolf = (Wolf)(Object)this;

        ItemStack armor = wolf.getItemBySlot(EquipmentSlot.CHEST);
        if (armor.isEmpty() || !armor.isDamageableItem()) {
            return;
        }

        int oldDamage = armor.getDamageValue();

        armor.hurtAndBreak(1, wolf, e -> {});

        int newDamage = armor.getDamageValue();
        int max = armor.getMaxDamage();
        int stageSize = max / 4;

        int oldStage = oldDamage / stageSize;
        int newStage = newDamage / stageSize;

        if (newStage > oldStage && newStage < 4) {
            wolf.level().playSound(
                    null,
                    wolf.blockPosition(),
                    ModSoundEvents.WOLF_ARMOR_CRACK.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        if (newDamage >= max) {
            wolf.level().playSound(
                    null,
                    wolf.blockPosition(),
                    ModSoundEvents.WOLF_ARMOR_BREAK.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void vb$repairWolfArmor(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Wolf wolf = (Wolf)(Object)this;
        ItemStack held = player.getItemInHand(hand);

        if (held.is(ModItems.ARMADILLO_SCUTE.get())) {

            ItemStack armor = wolf.getItemBySlot(EquipmentSlot.CHEST);
            if (armor.isEmpty() || !armor.isDamageableItem()) {
                return;
            }

            int dmg = armor.getDamageValue();

            if (dmg <= 0) {
                return;
            }

            int newDamage = Math.max(0, dmg - 16);
            armor.setDamageValue(newDamage);

            wolf.level().playSound(
                    null,
                    wolf.blockPosition(),
                    ModSoundEvents.WOLF_ARMOR_REPAIR.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }

            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Override
    protected void applyTamingSideEffects() {
        if (this.isTame()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0);
            this.setHealth(40.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
    }
}
