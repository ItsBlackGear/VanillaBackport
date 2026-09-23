package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.core.data.ModBuiltInLootTables;
import com.blackgear.vanillabackport.core.data.tags.ModStructureTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ChestLootGenerator extends SimpleFabricLootTableProvider {
    public ChestLootGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, LootContextParamSets.CHEST);
    }
    
    
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(
            ModBuiltInLootTables.ABANDONED_CAMP_BARREL,
            LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(UniformGenerator.between(4, 8))
                        .add(LootItem.lootTableItem(Items.ARROW).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.BONE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                        .add(LootItem.lootTableItem(Items.BOWL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.BREAD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.COAL).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                        .add(LootItem.lootTableItem(Items.COBWEB).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.LEATHER).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.STRING).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.WHEAT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.WHITE_CANDLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(ModItems.WHITE_CUSHION.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(ModBlocks.STRAW_BED.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.WOODEN_AXE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                )
        );
        output.accept(
            ModBuiltInLootTables.ABANDONED_CAMP_COMMON_CHEST,
            LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(UniformGenerator.between(4, 6))
                        .add(LootItem.lootTableItem(Items.ARROW).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4))))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.BONE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                        .add(LootItem.lootTableItem(Items.COBWEB).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.COMPASS).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.GUNPOWDER).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))
                        .add(LootItem.lootTableItem(Items.FISHING_ROD).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.FLINT_AND_STEEL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.LEAD).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.LEATHER).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.SADDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.WHITE_CANDLE).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2))
                        .add(LootItem.lootTableItem(Items.BOW).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.BUCKET).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_AXE.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_BOOTS.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_CHESTPLATE.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_LEGGINGS.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_SPEAR.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.COPPER_SWORD.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.SPYGLASS).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.SHEARS).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_BAMBOO_JUNGLE_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.bamboo_camp_map",
                                Biomes.BAMBOO_JUNGLE
                            )
                        ).add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_CHERRY_GROVE_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.cherry_grove_camp_map",
                                Biomes.CHERRY_GROVE
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_BIRCH_FOREST_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.birch_forest_camp_map",
                                Biomes.BIRCH_FOREST
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_DAPPLED_FOREST_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.dappled_forest_camp_map",
                                ModBiomes.DAPPLED_FOREST
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_FLOWER_FOREST_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.flower_forest_camp_map",
                                Biomes.FLOWER_FOREST
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_PALE_GARDEN_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.pale_garden_camp_map",
                                ModBiomes.PALE_GARDEN
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_SWAMP_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.swamp_camp_map",
                                Biomes.SWAMP
                            )
                        )
                        .add(
                            this.explorationMapItemExcludingBiome(
                                ModStructureTags.ON_ABANDONED_CAMP_WINDSWEPT_FOREST_MAPS,
                                MapDecoration.Type.BANNER_WHITE,
                                "filled_map.windswept_forest_camp_map",
                                Biomes.WINDSWEPT_FOREST
                            )
                        )
                )
        );
        output.accept(
            ModBuiltInLootTables.ABANDONED_CAMP_SECRET_CHEST,
            LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2))
                        .add(LootItem.lootTableItem(Items.DIAMOND).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(
                            LootItem.lootTableItem(Items.POTION)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(SetPotionFunction.setPotion(Potions.HEALING))
                        )
                        .add(
                            LootItem.lootTableItem(Items.POTION)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(SetPotionFunction.setPotion(Potions.LEAPING))
                        )
                        .add(
                            LootItem.lootTableItem(Items.POTION)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(SetPotionFunction.setPotion(Potions.NIGHT_VISION))
                        )
                        .add(
                            LootItem.lootTableItem(Items.POTION)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(SetPotionFunction.setPotion(Potions.SWIFTNESS))
                        )
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(UniformGenerator.between(4, 6))
                        .add(LootItem.lootTableItem(Items.MAP).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(Items.IRON_AXE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.IRON_BOOTS).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(Items.IRON_LEGGINGS).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .add(LootItem.lootTableItem(ModItems.IRON_SPEAR.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                )
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(this.explorationMapItem(ModStructureTags.ON_ANCIENT_CITY_MAPS, MapDecoration.Type.BLUE_MARKER))
//                        .add(this.explorationMapItem(StructureTags.ON_TRIAL_CHAMBERS_MAPS, MapDecorationTypes.TRIAL_CHAMBERS))
                        .add(this.explorationMapItem(ModStructureTags.ON_MINESHAFT_MAPS, MapDecoration.Type.BANNER_BROWN))
                        .add(this.explorationMapItem(ModStructureTags.ON_DESERT_PYRAMID_MAPS, MapDecoration.Type.BANNER_YELLOW))
                        .add(this.explorationMapItem(ModStructureTags.ON_JUNGLE_EXPLORER_MAPS, MapDecoration.Type.BANNER_GREEN))
                        .add(this.explorationMapItem(ModStructureTags.ON_OCEAN_RUIN_WARM_MAPS, MapDecoration.Type.BANNER_LIGHT_GRAY))
                        .add(this.explorationMapItem(StructureTags.ON_WOODLAND_EXPLORER_MAPS, MapDecoration.Type.MANSION))
                )
        );
    }
    
    private LootPoolSingletonContainer.Builder<?> explorationMapItemExcludingBiome(TagKey<Structure> structureTag, MapDecoration.Type decoration, String translationKey, ResourceKey<Biome> excludedBiome) {
        return this.explorationMapItem(structureTag, decoration, translationKey).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(excludedBiome)).invert());
    }
    
    private LootPoolSingletonContainer.Builder<?> explorationMapItem(TagKey<Structure> structureTag, MapDecoration.Type decoration) {
        return this.explorationMapItem(structureTag, decoration, null);
    }
    
    private LootPoolSingletonContainer.Builder<?> explorationMapItem(TagKey<Structure> structureTag, MapDecoration.Type decoration, @Nullable String translationKey) {
        var entry = LootItem.lootTableItem(Items.MAP).setWeight(1);
        
        entry.apply(ExplorationMapFunction.makeExplorationMap().setDestination(structureTag).setMapDecoration(decoration).setZoom((byte)1).setSkipKnownStructures(true));
        
        if (translationKey != null) {
            entry = entry.apply(SetNameFunction.setName(Component.translatable(translationKey)));
        }
        
        return entry;
    }
}