package com.blackgear.vanillabackport.common.api.wolf;

import com.blackgear.vanillabackport.common.registries.ModEntityDataSerializers;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.ServerLevelAccessor;

public record WolfSoundVariantsModule(SynchedEntityData data) {
    public static final EntityDataAccessor<WolfSoundVariant> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, ModEntityDataSerializers.WOLF_SOUND_VARIANT);

    public void defineSynchedData() {
        this.data.define(DATA_SOUND_VARIANT_ID, ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getOrThrow(WolfSoundVariants.CLASSIC));
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("sound_variant", ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getKey(this.getSoundVariant()).toString());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        WolfSoundVariant soundVariant = ModBuiltinRegistries.WOLF_SOUND_VARIANTS.get(ResourceLocation.tryParse(tag.getString("sound_variant")));
        if (soundVariant != null) {
            this.setSoundVariant(soundVariant);
        }
    }

    public WolfSoundVariant getSoundVariant() {
        return this.data.get(DATA_SOUND_VARIANT_ID);
    }

    public void setSoundVariant(WolfSoundVariant variant) {
        this.data.set(DATA_SOUND_VARIANT_ID, variant);
    }

    public void finalizeSpawn(ServerLevelAccessor level) {
        this.setSoundVariant(ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getRandomElement(level.getRandom()));
    }

    public void breedOffspring(Wolf child, Wolf parent) {
        WolfSoundVariantHolder.of(child).setSoundVariant(ModBuiltinRegistries.WOLF_SOUND_VARIANTS.getRandomElement(parent.getRandom()));
    }
}