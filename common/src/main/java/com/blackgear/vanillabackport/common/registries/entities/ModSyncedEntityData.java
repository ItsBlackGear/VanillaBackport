package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.common.data.entity.SyncedDataKey;
import com.blackgear.platform.common.data.entity.SyncedDataKey.SyncMode;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariants;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cow.CowVariants;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.pig.PigVariants;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfVariants;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class ModSyncedEntityData {
    public static final SyncedDataKey<RegistryKey<CowVariant>> COW_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("cow_variants"),
        RegistryKey.codec(),
        () -> CowVariants.TEMPERATE,
        SyncMode.TRACKING_ONLY);
    public static final SyncedDataKey<RegistryKey<PigVariant>> PIG_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("pig_variants"),
        RegistryKey.codec(),
        () -> PigVariants.TEMPERATE,
        SyncMode.TRACKING_ONLY);
    public static final SyncedDataKey<RegistryKey<ChickenVariant>> CHICKEN_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("chicken_variants"),
        RegistryKey.codec(),
        () -> ChickenVariants.TEMPERATE,
        SyncMode.TRACKING_ONLY);
    public static final SyncedDataKey<RegistryKey<WolfVariant>> WOLF_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("wolf_variants"),
        RegistryKey.codec(),
        () -> WolfVariants.PALE,
        SyncMode.TRACKING_ONLY);
    public static final SyncedDataKey<RegistryKey<CatDataVariant>> CAT_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("cat_variants"),
        RegistryKey.codec(),
        () -> RegistryKey.of("minecraft", "tabby"),
        SyncMode.TRACKING_ONLY);
    public static final SyncedDataKey<RegistryKey<FrogDataVariant>> FROG_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("frog_variants"),
        RegistryKey.codec(),
        () -> RegistryKey.of("minecraft", "temperate"),
        SyncMode.TRACKING_ONLY);

    public static final SyncedDataKey<RegistryKey<WolfSoundVariant>> WOLF_SOUND_VARIANTS = SyncedDataKey.create(
        VanillaBackport.resource("wolf_sound_variants"),
        RegistryKey.codec(),
        () -> WolfSoundVariants.CLASSIC,
        SyncMode.TRACKING_ONLY);
    
    public static void init() {}
}