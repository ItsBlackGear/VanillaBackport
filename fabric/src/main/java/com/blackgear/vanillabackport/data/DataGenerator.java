package com.blackgear.vanillabackport.data;

import com.blackgear.vanillabackport.common.registries.entities.ModDamageTypes;
import com.blackgear.vanillabackport.common.registries.enchantment.ModEnchantments;
import com.blackgear.vanillabackport.common.registries.items.ModJukeboxSongs;
import com.blackgear.vanillabackport.common.registries.items.ModPaintingVariants;
import com.blackgear.vanillabackport.common.registries.items.ModTrimMaterials;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.registries.worldgen.ModNoises;
import com.blackgear.vanillabackport.common.worldgen.features.ChaosCubedFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.ChaosCubedPlacements;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.data.client.LangGenerator;
import com.blackgear.vanillabackport.data.client.ModelGenerator;
import com.blackgear.vanillabackport.data.server.advancement.AdvancementGenerator;
import com.blackgear.vanillabackport.data.server.builder.*;
import com.blackgear.vanillabackport.data.server.loot.BlockLootGenerator;
import com.blackgear.vanillabackport.data.server.loot.EntityLootGenerator;
import com.blackgear.vanillabackport.data.server.loot.GiftLootGenerator;
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
        pack.addProvider(AdvancementGenerator::new);

        pack.addProvider(BlockLootGenerator::new);
        pack.addProvider(GiftLootGenerator::new);
        pack.addProvider(EntityLootGenerator::new);

        pack.addProvider(RecipeGenerator::new);

        pack.addProvider(BiomeTagGenerator::new);
        pack.addProvider(BlockTagGenerator::new);
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(EntityTypeTagGenerator::new);
        pack.addProvider(DamageTypeTagGenerator::new);
        pack.addProvider(PaintingVariantTagGenerator::new);

        pack.addProvider(PaintVariantsGenerator::new);
        pack.addProvider(TrimMaterialGenerator::new);
        pack.addProvider(BiomeGenerator::new);
        pack.addProvider(NoiseGenerator::new);
        pack.addProvider(DamageTypeGenerator::new);
        pack.addProvider(ConfiguredFeatureGenerator::new);
        pack.addProvider(PlacedFeatureGenerator::new);
        pack.addProvider(JukeboxSongGenerator::new);
        pack.addProvider(EnchantmentGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder builder) {
        builder.add(Registries.PAINTING_VARIANT, ModPaintingVariants.REGISTRIES::bootstrap);
        builder.add(Registries.TRIM_MATERIAL, ModTrimMaterials.REGISTRIES::bootstrap);
        builder.add(Registries.JUKEBOX_SONG, ModJukeboxSongs.REGISTRIES::bootstrap);
        builder.add(Registries.BIOME, ModBiomes.REGISTRIES::bootstrap);
        builder.add(Registries.NOISE, ModNoises.REGISTRIES::bootstrap);
        builder.add(Registries.DAMAGE_TYPE, ModDamageTypes.REGISTRIES::bootstrap);
        builder.add(Registries.ENCHANTMENT, ModEnchantments.REGISTRIES::bootstrap);
        builder.add(Registries.CONFIGURED_FEATURE, TheGardenAwakensFeatures.REGISTRIES::bootstrap);
        builder.add(Registries.CONFIGURED_FEATURE, SpringToLifeFeatures.REGISTRIES::bootstrap);
        builder.add(Registries.CONFIGURED_FEATURE, ChaosCubedFeatures.REGISTRIES::bootstrap);
        builder.add(Registries.PLACED_FEATURE, TheGardenAwakensPlacements.REGISTRIES::bootstrap);
        builder.add(Registries.PLACED_FEATURE, SpringToLifePlacements.REGISTRIES::bootstrap);
        builder.add(Registries.PLACED_FEATURE, ChaosCubedPlacements.REGISTRIES::bootstrap);
    }
}