package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.sound.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfurcube.SulfurCubeArchetype;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class ModBuiltinRegistries {
    public static final BuiltInCoreRegistry<WolfSoundVariant> WOLF_SOUND_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.WOLF_SOUND_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<CowVariant> COW_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.COW_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<ChickenVariant> CHICKEN_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.CHICKEN_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<PigVariant> PIG_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.PIG_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<FrogDataVariant> FROG_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.FROG_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<WolfDataVariant> WOLF_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.WOLF_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<CatDataVariant> CAT_VARIANTS = new BuiltInCoreRegistry<>(ModRegistries.CAT_VARIANT.get(), VanillaBackport.NAMESPACE);
    public static final BuiltInCoreRegistry<SulfurCubeArchetype> SULFUR_CUBE_ARCHETYPES = new BuiltInCoreRegistry<>(ModRegistries.SULFUR_CUBE_ARCHETYPE.get(), VanillaBackport.NAMESPACE);
}