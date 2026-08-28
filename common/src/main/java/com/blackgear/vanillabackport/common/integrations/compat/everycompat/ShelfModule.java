package com.blackgear.vanillabackport.common.integrations.compat.everycompat;

import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import com.blackgear.vanillabackport.common.level.blocks.ShelfBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodChildKeys;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

public class ShelfModule extends EveryCompatModule {
    public final SimpleEntrySet<WoodType, Block> shelf;
    
    public ShelfModule(String modId) {
        super(modId, VanillaBackport.MOD_ID);
        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.FUNCTIONAL_BLOCKS;
        
        this.shelf = SimpleEntrySet.builder(WoodType.class, "shelf",
                () -> BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace("oak_shelf")),
                () -> VanillaWoodTypes.OAK,
                w -> new ShelfBlock(BlockBehaviour.Properties.of()
                    .mapColor(w.planks.defaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .sound(ModSoundTypes.WOODEN_SHELF)
                    .ignitedByLava()
                    .strength(2.0F, 3.0F)
                ))
            .requiresChildren(VanillaWoodChildKeys.STRIPPED_LOG)
            .addTexture(ResourceLocation.withDefaultNamespace("block/oak_shelf"), PaletteStrategies.PLANKS_REMOVE_2_DARKEST)
            .includeModelsBlock(
                ResourceLocation.withDefaultNamespace("block/oak_shelf"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_center"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_inventory"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_left"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_right"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_unconnected"),
                ResourceLocation.withDefaultNamespace("block/oak_shelf_unpowered")
            )
            .addModelTransform(t -> {
                t.addModifier((s, id, woodType) ->
                    s.replace(
                            "\"minecraft:block/oak_shelf\"",
                            "\"" + id.withPrefix("block/") + "\""
                        )
                        .replaceAll("\"minecraft:block/oak_shelf_([a-z]+)\"", "\"" + id.withPrefix("block/") + "_$1\"")
                );
            })
            .addTile(ModBlockEntities.SHELF)
            .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
            .addTag(ModBlockTags.WOODEN_SHELVES, Registries.BLOCK)
            .addTag(ModItemTags.WOODEN_SHELVES, Registries.ITEM)
            .setTabKey(tab)
            .addRecipe(ResourceLocation.withDefaultNamespace("oak_shelf"))
            .build();
        this.addEntry(this.shelf);
    }
}
