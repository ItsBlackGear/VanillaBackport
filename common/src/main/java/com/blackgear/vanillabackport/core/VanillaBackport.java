package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;
import com.blackgear.platform.core.ModInstance;
import com.blackgear.platform.core.util.config.ModConfig;
import com.blackgear.vanillabackport.client.ClientConfig;
import com.blackgear.vanillabackport.client.ClientSetup;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.CommonConfig;
import com.blackgear.vanillabackport.common.CommonSetup;
import com.blackgear.vanillabackport.common.api.variant.SpawnConditions;
import com.blackgear.vanillabackport.common.registries.*;
import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.core.data.DataTransformation;
import com.blackgear.vanillabackport.core.data.tags.*;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class VanillaBackport {
    public static final String MOD_ID = "vanillabackport";
    public static final String NAMESPACE = "minecraft";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ClientConfig CLIENT_CONFIG = Environment.registerSafeConfig(MOD_ID, ModConfig.Type.CLIENT, ClientConfig::new);
    public static final CommonConfig COMMON_CONFIG = Environment.registerSafeConfig(MOD_ID, ModConfig.Type.COMMON, CommonConfig::new);
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
        .client(ClientSetup::setup)
        .postClient(ClientSetup::asyncSetup)
        .common(CommonSetup::setup)
        .postCommon(CommonSetup::asyncSetup)
        .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
        NetworkHandler.bootstrap();

        ModBlockTags.TAGS.register();
        ModItemTags.TAGS.register();
        ModBiomeTags.TAGS.register();
        ModEntityTypeTags.TAGS.register();

        ModParticles.PARTICLES.register();

        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModEntities.ENTITIES.register();
        ModSensorTypes.SENSOR_TYPES.register();

        ModSoundEvents.SOUNDS.register();
        ModSoundTypes.SOUNDS.register();

        ModRecipeSerializers.SERIALIZERS.register();

        ModCreativeTabs.TABS.register();
        ModPaintingVariants.VARIANTS.register();
        ModBuiltinRegistries.WOLF_SOUND_VARIANTS.register();
        ModBuiltinRegistries.COW_VARIANTS.register();
        ModBuiltinRegistries.CHICKEN_VARIANTS.register();
        ModBuiltinRegistries.PIG_VARIANTS.register();
        SpawnConditions.CONDITIONS.register();

        ModBiomes.BIOMES.register();
        ModFeatures.FEATURES.register();
        ModTreeDecorators.DECORATORS.register();
        SpringToLifeFeatures.FEATURES.register();
        TheGardenAwakensFeatures.FEATURES.register();
        SpringToLifePlacements.FEATURES.register();
        TheGardenAwakensPlacements.FEATURES.register();

        ModEntityDataSerializers.SERIALIZERS.register();
        DataTransformation.bootstrap();
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation vanilla(String path) {
        return new ResourceLocation(NAMESPACE, path);
    }
}