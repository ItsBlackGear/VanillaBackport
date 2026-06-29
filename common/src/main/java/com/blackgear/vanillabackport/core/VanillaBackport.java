package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;
import com.blackgear.platform.core.ModInstance;
import com.blackgear.platform.core.networking.Networking;
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
import com.blackgear.vanillabackport.common.registries.*;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModSensorTypes;
import com.blackgear.vanillabackport.common.registries.items.ModArmorMaterials;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.worldgen.*;
import com.blackgear.vanillabackport.core.data.tags.*;
import com.blackgear.vanillabackport.core.network.ServerboundSelectBundleItemPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public final class VanillaBackport {
    public static final String MOD_ID = "vanillabackport";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    /**
     * [WARNING] for registering custom features, ALWAYS use your own ID instead.
     * <br><p>
     * this mod attempts to make responsible use of the vanilla namespace, but it's highly encouraged to avoid this.
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

        ModBlockTags.TAGS.register();
        ModItemTags.TAGS.register();
        ModBiomeTags.TAGS.register();
        ModEntityTypeTags.TAGS.register();
        ModDamageTypeTags.TAGS.register();

        ModValueProviders.REGISTRIES.register();
        ModMaterialRules.REGISTRIES.registrar();
        ModMaterialConditions.REGISTRIES.registrar();

        ModAttributes.REGISTRIES.register();
        ModArmorMaterials.REGISTRIES.register();
        
        ModParticles.PARTICLES.register();

        ModDataComponents.REGISTRIES.register();
        ModBlocks.REGISTRIES.register();
        ModItems.REGISTRIES.register();
        ModItems.ITEMS.register();
        ModBlockEntities.REGISTRIES.register();
        ModEntityTypes.REGISTRIES.register();
        ModSensorTypes.REGISTRIES.register();

        ModSoundEvents.REGISTRIES.register();
        ModSoundTypes.REGISTRIES.register();

        ModRecipeSerializers.REGISTRIES.register();
        ModCreativeTabs.TABS.register();
        SpawnConditions.REGISTRIES.register();

        ModFeatures.REGISTRIES.register();
        ModTreeDecorators.REGISTRIES.register();

        Networking.register(registrar -> registrar.registerToServer(
            ServerboundSelectBundleItemPacket.TYPE,
            ServerboundSelectBundleItemPacket.STREAM_CODEC,
            ServerboundSelectBundleItemPacket::handler
        ));
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}