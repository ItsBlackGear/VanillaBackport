package com.blackgear.vanillabackport.core.registries;

import com.blackgear.platform.core.RegistryBuilder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilusVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCubeArchetype;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ModRegistries {
    public static final RegistryBuilder BUILDER = RegistryBuilder.create(VanillaBackport.MOD_ID);

    public static final ResourceKey<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT_KEY = BUILDER.resource("wolf_sound_variant");
    public static final Supplier<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT = BUILDER.registry(WOLF_SOUND_VARIANT_KEY);

    public static final ResourceKey<Registry<CowVariant>> COW_VARIANT_KEY = BUILDER.resource("cow_variant");
    public static final Supplier<Registry<CowVariant>> COW_VARIANT = BUILDER.registry(COW_VARIANT_KEY);

    public static final ResourceKey<Registry<ChickenVariant>> CHICKEN_VARIANT_KEY = BUILDER.resource("chicken_variant");
    public static final Supplier<Registry<ChickenVariant>> CHICKEN_VARIANT = BUILDER.registry(CHICKEN_VARIANT_KEY);

    public static final ResourceKey<Registry<PigVariant>> PIG_VARIANT_KEY = BUILDER.resource("pig_variant");
    public static final Supplier<Registry<PigVariant>> PIG_VARIANT = BUILDER.registry(PIG_VARIANT_KEY);

    public static final ResourceKey<Registry<WolfDataVariant>> WOLF_VARIANT_KEY = BUILDER.resource("wolf_variant");
    public static final Supplier<Registry<WolfDataVariant>> WOLF_VARIANT = BUILDER.registry(WOLF_VARIANT_KEY);

    public static final ResourceKey<Registry<FrogDataVariant>> FROG_VARIANT_KEY = BUILDER.resource("frog_variant");
    public static final Supplier<Registry<FrogDataVariant>> FROG_VARIANT = BUILDER.registry(FROG_VARIANT_KEY);

    public static final ResourceKey<Registry<CatDataVariant>> CAT_VARIANT_KEY = BUILDER.resource("cat_variant");
    public static final Supplier<Registry<CatDataVariant>> CAT_VARIANT = BUILDER.registry(CAT_VARIANT_KEY);

    public static final ResourceKey<Registry<MapCodec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE_KEY = BUILDER.resource("spawn_condition_type");
    public static final Supplier<Registry<MapCodec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE = BUILDER.registry(SPAWN_CONDITION_TYPE_KEY);

    public static final ResourceKey<Registry<SulfurCubeArchetype>> SULFUR_CUBE_ARCHETYPES_KEY = BUILDER.resource("sulfur_cube_archetypes");
    public static final Supplier<Registry<SulfurCubeArchetype>> SULFUR_CUBE_ARCHETYPE = BUILDER.registry(SULFUR_CUBE_ARCHETYPES_KEY);
    
    public static final ResourceKey<Registry<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT_KEY = BUILDER.resource("zombie_nautilus_variant");
    public static final Supplier<Registry<ZombieNautilusVariant>> ZOMBIE_NAUTILUS_VARIANT = BUILDER.registry(ZOMBIE_NAUTILUS_VARIANT_KEY);
}