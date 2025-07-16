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
import com.blackgear.vanillabackport.common.registries.*;
import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class VanillaBackport {
    public static final String MOD_ID = "vanillabackport";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ClientConfig CLIENT_CONFIG = Environment.registerUnsafeConfig(MOD_ID, ModConfig.Type.CLIENT, ClientConfig::new);
    public static final CommonConfig CONFIG = Environment.registerUnsafeConfig(MOD_ID, ModConfig.Type.COMMON, CommonConfig::new);
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
        .client(ClientSetup::setup)
        .postClient(ClientSetup::asyncSetup)
        .common(CommonSetup::setup)
        .postCommon(CommonSetup::asyncSetup)
        .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
        NetworkHandler.bootstrap();

        ModParticles.PARTICLES.register();

        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModEntities.ENTITIES.register();
        ModSensorTypes.SENSOR_TYPES.register();

        ModSoundEvents.SOUNDS.register();
        ModSoundTypes.SOUNDS.register();

        ModCreativeTabs.TABS.register();
        ModPaintingVariants.VARIANTS.register();

        ModBiomes.BIOMES.register();
        ModFeatures.FEATURES.register();
        ModTreeDecorators.DECORATORS.register();
        SpringToLifeFeatures.FEATURES.register();
        TheGardenAwakensFeatures.FEATURES.register();
        SpringToLifePlacements.FEATURES.register();
        TheGardenAwakensPlacements.FEATURES.register();

        ModBlockTags.TAGS.register();
        ModItemTags.TAGS.register();
        ModEntityTypeTags.TAGS.register();
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}