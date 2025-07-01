package com.blackgear.vanillabackport.data;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.registries.ModTrimMaterials;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.data.client.LangGenerator;
import com.blackgear.vanillabackport.data.client.ModelGenerator;
import com.blackgear.vanillabackport.data.server.builder.*;
import com.blackgear.vanillabackport.data.server.loot.BlockLootGenerator;
import com.blackgear.vanillabackport.data.server.recipe.RecipeGenerator;
import com.blackgear.vanillabackport.data.server.tags.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        // CLIENT SIDE
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(LangGenerator::new);

        // SERVER SIDE
        pack.addProvider(BlockLootGenerator::new);

        pack.addProvider(RecipeGenerator::new);

        pack.addProvider(BiomeTagGenerator::new);
        pack.addProvider(BlockTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(EntityTypeTagGenerator::new);
        pack.addProvider(PaintingVariantTagGenerator::new);

        pack.addProvider(TrimMaterialGenerator::new);
        pack.addProvider(BiomeGenerator::new);
        pack.addProvider(ConfiguredFeatureGenerator::new);
        pack.addProvider(PlacedFeatureGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap);
        builder.add(Registries.BIOME, ModBiomes::bootstrap);
        builder.add(Registries.CONFIGURED_FEATURE, TheGardenAwakensFeatures::bootstrap);
        builder.add(Registries.PLACED_FEATURE, TheGardenAwakensPlacements::bootstrap);
    }
}