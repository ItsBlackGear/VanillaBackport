package com.blackgear.vanillabackport.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.integration.TradeIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.integrations.LootIntegrations;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariants;
import com.blackgear.vanillabackport.common.integrations.BlockIntegrations;
import com.blackgear.vanillabackport.common.integrations.MobIntegrations;
import com.blackgear.vanillabackport.common.integrations.TradeIntegrations;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cat.CatDataVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.frog.FrogDataVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilusVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilusVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.pig.PigVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.wolf.WolfVariants;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCubeArchetype;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCubeArchetypes;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.integrations.worldgen.BiomeGeneration;
import com.blackgear.vanillabackport.common.integrations.worldgen.WorldGeneration;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.animal.Parrot;

public class CommonSetup {
    public static void setup() {
        ResourceReloadManager.registerServer(event -> {
            event.register(VanillaBackport.resource("wolf_sound_variant"), WolfSoundVariants.REGISTRIES, WolfSoundVariant.CODEC);
            event.register(VanillaBackport.resource("cow_variant"), CowVariants.REGISTRIES, CowVariant.CODEC);
            event.register(VanillaBackport.resource("chicken_variant"), ChickenVariants.REGISTRIES, ChickenVariant.CODEC);
            event.register(VanillaBackport.resource("pig_variant"), PigVariants.REGISTRIES, PigVariant.CODEC);
            event.register(VanillaBackport.resource("wolf_variant"), WolfVariants.REGISTRIES, WolfVariant.CODEC);
            event.register(VanillaBackport.resource("frog_variant"), FrogDataVariants.REGISTRIES, FrogDataVariant.CODEC);
            event.register(VanillaBackport.resource("cat_variant"), CatDataVariants.REGISTRIES, CatDataVariant.CODEC);
            event.register(VanillaBackport.resource("sulfur_cube_archetype"), SulfurCubeArchetypes.REGISTRIES, SulfurCubeArchetype.CODEC);
            event.register(VanillaBackport.resource("zombie_nautilus_variant"), ZombieNautilusVariants.REGISTRIES, ZombieNautilusVariant.CODEC);
        });

        MobIntegration.registerIntegrations(MobIntegrations::bootstrap);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            BiomeManager.add(WorldGeneration::bootstrap);
            BiomePlacement.registerBiomePlacements(BiomeGeneration::bootstrap);
            BlockIntegration.registerIntegrations(BlockIntegrations::bootstrap);
            TradeIntegration.registerVillagerTrades(TradeIntegrations::bootstrap);
            Parrot.MOB_SOUND_MAP.put(ModEntityTypes.CREAKING.get(), ModSoundEvents.PARROT_IMITATE_CREAKING.get());
            Parrot.MOB_SOUND_MAP.put(ModEntityTypes.PARCHED.get(), ModSoundEvents.PARROT_IMITATE_PARCHED.get());
            Parrot.MOB_SOUND_MAP.put(ModEntityTypes.CAMEL_HUSK.get(), ModSoundEvents.PARROT_IMITATE_CAMEL_HUSK.get());
            Parrot.MOB_SOUND_MAP.put(ModEntityTypes.ZOMBIE_NAUTILUS.get(), ModSoundEvents.PARROT_IMITATE_ZOMBIE_NAUTILUS.get());
        });

        LootModifier.modify(LootIntegrations.INSTANCE);
    }
}