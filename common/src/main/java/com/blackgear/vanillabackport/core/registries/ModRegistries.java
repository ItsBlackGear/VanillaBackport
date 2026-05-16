package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.api.registrar.RegistrarBuilder;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.*;
import com.blackgear.vanillabackport.common.level.entities.sulfurcube.SulfurCubeArchetype;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;

public class ModRegistries {
    public static final RegistrarBuilder REGISTRAR = RegistrarBuilder.create(VanillaBackport.NAMESPACE);
    public static final RegistrarBuilder INTERNAL_REGISTRAR = RegistrarBuilder.create(VanillaBackport.MOD_ID);

    public static final Registry<MapCodec<? extends SpawnCondition>> SPAWN_CONDITION_TYPE = REGISTRAR.registerSimple("spawn_condition_type");
    public static final Registry<WolfSoundVariant> WOLF_SOUND_VARIANT = REGISTRAR.registerSimple("wolf_sound_variant");
    public static final Registry<CowVariant> COW_VARIANT = REGISTRAR.registerSimple("cow_variant");
    public static final Registry<ChickenVariant> CHICKEN_VARIANT = REGISTRAR.registerSimple("chicken_variant");
    public static final Registry<PigVariant> PIG_VARIANT = REGISTRAR.registerSimple("pig_variant");
    public static final Registry<WolfDataVariant> WOLF_VARIANT = INTERNAL_REGISTRAR.registerSimple("wolf_variant");
    public static final Registry<FrogDataVariant> FROG_VARIANT = INTERNAL_REGISTRAR.registerSimple("frog_variant");
    public static final Registry<CatDataVariant> CAT_VARIANT = INTERNAL_REGISTRAR.registerSimple("cat_variant");
    public static final Registry<SulfurCubeArchetype> SULFUR_CUBE_ARCHETYPE = INTERNAL_REGISTRAR.registerSimple("sulfur_cube_archetype");
}