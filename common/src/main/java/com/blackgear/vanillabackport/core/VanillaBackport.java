package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;
import com.blackgear.platform.core.ModInstance;
import com.blackgear.platform.core.util.config.ConfigLoader;
import com.blackgear.platform.core.util.config.ModConfig;
import com.blackgear.vanillabackport.client.ClientConfig;
import com.blackgear.vanillabackport.client.ClientSetup;
import com.blackgear.vanillabackport.client.registries.ModCreativeTabs;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.CommonConfig;
import com.blackgear.vanillabackport.common.CommonSetup;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnConditions;
import com.blackgear.vanillabackport.common.integrations.compat.everycompat.EveryCompatHandler;
import com.blackgear.vanillabackport.common.registries.*;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.blocks.ModPoiTypes;
import com.blackgear.vanillabackport.common.registries.entities.*;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.items.ModPaintingVariants;
import com.blackgear.vanillabackport.common.registries.worldgen.*;
import com.blackgear.vanillabackport.core.data.tags.*;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class VanillaBackport {
    public static final String MOD_ID = "vanillabackport";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    /**
     * [WARNING] for registering custom features, ALWAYS use your own ID instead.
     * <br><p>
     * This mod attempts to make responsible use of the vanilla namespace, but it's highly encouraged to avoid this.
     */
    public static final String NAMESPACE = "minecraft";
    
    public static final ClientConfig CLIENT_CONFIG = Environment.registerConfig(MOD_ID, ModConfig.Type.CLIENT, ClientConfig::new);
    public static final CommonConfig COMMON_CONFIG = Environment.registerConfig(MOD_ID, ModConfig.Type.COMMON, CommonConfig::new);
    
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
        .client(ClientSetup::setup)
        .postClient(ClientSetup::asyncSetup)
        .common(CommonSetup::setup)
        .postCommon(CommonSetup::asyncSetup)
        .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();
        ConfigLoader.bootstrap();

        ModCriteriaTriggers.bootstrap();

        ModBlockTags.TAGS.register();
        ModItemTags.TAGS.register();
        ModBiomeTags.TAGS.register();
        ModBiomeTags.CONVENTIONAL.register();
        ModEntityTypeTags.TAGS.register();
        ModDamageTypeTags.TAGS.register();

        ModValueProviders.REGISTRIES.register();
        ModMaterialRules.REGISTRIES.registrar();
        ModMaterialConditions.REGISTRIES.registrar();

        ModAttributes.REGISTRIES.registrar();
        ModParticles.REGISTRIES.register();

        ModBlocks.REGISTRIES.register();
        ModBlocks.HOLDERS.register();
        ModItems.REGISTRIES.register();
        ModItems.HOLDERS.register();
        ModBlockEntities.REGISTRIES.register();
        ModBlockEntities.HOLDERS.register();
        
        ModPoiTypes.REGISTRIES.register();
        
        ModEntityTypes.REGISTRIES.register();
        ModMemoryModuleTypes.REGISTRIES.register();
        ModSensorTypes.REGISTRIES.register();
        
        ModSoundEvents.REGISTRIES.register();
        ModSoundTypes.REGISTRIES.register();

        ModRecipeSerializers.SERIALIZERS.register();
        ModCreativeTabs.TABS.register();
        ModPaintingVariants.REGISTRIES.register();
        SpawnConditions.REGISTRIES.registrar();

        ModFeatures.REGISTRIES.register();
        ModTreeDecorators.REGISTRIES.register();

        ModEntityDataSerializers.SERIALIZERS.register();
        ModGameRules.bootstrap();
        
        NetworkHandler.bootstrap();
        
        if (ModChecker.EVERY_COMPAT) EveryCompatHandler.bootstrap();
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}