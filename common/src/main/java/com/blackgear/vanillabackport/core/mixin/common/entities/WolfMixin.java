package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.variant.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.VariantHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.api.wolf.BackportedWolvesConversion;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariants;
import com.blackgear.vanillabackport.common.level.entities.wolf.ModCrackiness;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariants;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfSoundVariantsModule;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
public abstract class WolfMixin extends TamableAnimalMixin implements NeutralMob, WolfSoundVariantHolder, VariantHolder<WolfVariant> {
    @Unique
    private static final EntityDataAccessor<String> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
    @Unique
    private static final EntityDataAccessor<String> DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.STRING);
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
            VariantHolder.vb$trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void vb$defineSynchedData(CallbackInfo ci) {
        this.entityData.define(DATA_SOUND_VARIANT_ID, VariantUtils.getDefaultID(ModBuiltinRegistries.WOLF_SOUND_VARIANTS, WolfSoundVariants.CLASSIC));
        this.entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultID(ModBuiltinRegistries.WOLF_VARIANTS, WolfVariants.PALE));
    }

    @Override
    public WolfVariant vb$getVariant() {
        return VariantUtils.getVariant(ModBuiltinRegistries.WOLF_VARIANTS, this.entityData.get(DATA_VARIANT_ID));
    }

    @Override
    public void vb$setVariant(WolfVariant variant) {
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
        VariantUtils.selectWolfVariantToSpawn(SpawnContext.create(level, this.blockPosition()), ModBuiltinRegistries.WOLF_VARIANTS, WolfVariants.PALE)
            .ifPresent(this::vb$setVariant);
    }

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void vb$getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        if (this.canArmorAbsorb(source)) {
            cir.setReturnValue(ModSoundEvents.WOLF_ARMOR_DAMAGE.get());
        }

        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & WolfSoundVariantHolder) (Object) this);
        if (result != null) cir.setReturnValue(result);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.canArmorAbsorb(damageSource)) {
            super.actuallyHurt(damageSource, damageAmount);
        } else {
            ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
            int prevDamage = stack.getDamageValue();
            int max = stack.getMaxDamage();
            stack.hurtAndBreak(Mth.ceil(damageAmount), this, wolf -> wolf.broadcastBreakEvent(EquipmentSlot.CHEST));
            if (ModCrackiness.WOLF_ARMOR.byDamage(prevDamage, max) != ModCrackiness.WOLF_ARMOR.byDamage(stack)) {
                this.playSound(ModSoundEvents.WOLF_ARMOR_CRACK.get());
                if (this.level() instanceof ServerLevel level) {
                    level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, ModItems.ARMADILLO_SCUTE.get().getDefaultInstance()), this.getX(), this.getY() + 1.0, this.getZ(), 20, 0.2, 0.1, 0.2, 0.1);
                }
            }
        }
    }

    @Unique
    private boolean canArmorAbsorb(DamageSource source) {
        return this.hasArmor() && !source.is(DamageTypeTags.BYPASSES_ARMOR);
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

    @Override
    protected void hurtArmor(DamageSource damageSource, float damageAmount) {
        if (damageAmount > 0.0F) {
            int i = (int) Math.max(1.0F, damageAmount / 4.0F);

            ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (stack.getItem() instanceof WolfArmorItem) {
                stack.hurtAndBreak(i, this, wolf -> wolf.broadcastBreakEvent(EquipmentSlot.CHEST));
                if (stack.isEmpty()) {
                    this.playSound(ModSoundEvents.WOLF_ARMOR_BREAK.get());
                    this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                }
            }
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void vb$mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.level().isClientSide) {
            if (this.isTame()) {
                if (stack.is(ModItems.WOLF_ARMOR.get()) && this.isOwnedBy(player) && this.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && !this.isBaby()) {
                    this.setItemSlot(EquipmentSlot.CHEST, stack.copyWithCount(1));
                    this.playSound(ModSoundEvents.ARMOR_EQUIP_WOLF.get());
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                } else if (stack.is(Items.SHEARS)
                    && this.isOwnedBy(player)
                    && this.hasArmor()
                    && (!EnchantmentHelper.hasBindingCurse(this.getItemBySlot(EquipmentSlot.CHEST)) || player.isCreative())) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                    this.playSound(ModSoundEvents.ARMOR_UNEQUIP_WOLF.get());
                    ItemStack armor = this.getItemBySlot(EquipmentSlot.CHEST);
                    this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                    this.spawnAtLocation(armor);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                } else if (stack.is(ModItems.ARMADILLO_SCUTE.get())
                    && this.isInSittingPose()
                    && this.hasArmor()
                    && this.isOwnedBy(player)) {

                    ItemStack armor = this.getItemBySlot(EquipmentSlot.CHEST);
                    if (armor.isDamaged()) {
                        int repair = Mth.ceil(armor.getMaxDamage() * 0.125F);
                        int current = armor.getDamageValue();
                        int newDamage = Math.max(0, current - repair);

                        if (newDamage < current) {
                            armor.setDamageValue(newDamage);
                            this.playSound(ModSoundEvents.WOLF_ARMOR_REPAIR.get());
                            if (!player.getAbilities().instabuild) {
                                stack.shrink(1);
                            }

                            cir.setReturnValue(InteractionResult.SUCCESS);
                        }
                    }
                }
            }
        }
    }

    @Unique
    private boolean hasArmor() {
        ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
        return !stack.isEmpty() && stack.is(ModItems.WOLF_ARMOR.get());
    }
}
