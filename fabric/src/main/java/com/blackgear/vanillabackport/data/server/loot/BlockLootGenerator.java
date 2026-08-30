package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.level.blocks.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.blocks.MossyCarpetBlock;
import com.blackgear.vanillabackport.common.level.blocks.SegmentableBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.IntStream;

public class BlockLootGenerator extends FabricBlockLootTableProvider {
    public BlockLootGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        // The Garden Awakens
        this.dropSelf(ModBlocks.PALE_OAK_PLANKS.get());
        this.dropSelf(ModBlocks.PALE_OAK_SAPLING.get());
        this.dropSelf(ModBlocks.PALE_OAK_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        this.dropSelf(ModBlocks.PALE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
        this.dropSelf(ModBlocks.PALE_OAK_SIGN.getFirst().get());
        this.dropSelf(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get());
        this.dropSelf(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.PALE_OAK_TRAPDOOR.get());
        this.dropSelf(ModBlocks.PALE_OAK_BUTTON.get());
        this.dropSelf(ModBlocks.PALE_OAK_STAIRS.get());
        this.dropSelf(ModBlocks.PALE_OAK_FENCE_GATE.get());
        this.dropSelf(ModBlocks.PALE_OAK_FENCE.get());
        this.add(ModBlocks.PALE_OAK_SLAB.get(), this::createSlabItemTable);
        this.add(ModBlocks.PALE_OAK_DOOR.get(), this::createDoorTable);
        this.add(ModBlocks.PALE_OAK_LEAVES.get(), block -> this.createLeavesDrops(block, ModBlocks.PALE_OAK_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        this.dropSelf(ModBlocks.OPEN_EYEBLOSSOM.get());
        this.dropSelf(ModBlocks.CLOSED_EYEBLOSSOM.get());

        this.add(ModBlocks.PALE_MOSS_CARPET.get(), this::createMossyCarpetBlockDrops);
        this.add(ModBlocks.PALE_HANGING_MOSS.get(), this::createShearsOrSilkTouchOnlyDrop);
        this.dropSelf(ModBlocks.PALE_MOSS_BLOCK.get());

        this.dropPottedContents(ModBlocks.POTTED_PALE_OAK_SAPLING.get());
        this.dropPottedContents(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get());
        this.dropPottedContents(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get());

        this.dropSelf(ModBlocks.RESIN_BLOCK.get());
        this.dropSelf(ModBlocks.RESIN_BRICKS.get());
        this.dropSelf(ModBlocks.RESIN_BRICK_WALL.get());
        this.dropSelf(ModBlocks.RESIN_BRICK_STAIRS.get());
        this.dropSelf(ModBlocks.CHISELED_RESIN_BRICKS.get());
        this.add(ModBlocks.RESIN_BRICK_SLAB.get(), this::createSlabItemTable);
        this.add(ModBlocks.RESIN_CLUMP.get(), this::createMultifaceBlockDrops);

        this.add(
            ModBlocks.CREAKING_HEART.get(),
            block -> createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                    block,
                    LootItem.lootTableItem(ModBlocks.RESIN_CLUMP.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                        .apply(LimitCount.limitCount(IntRange.upperBound(9)))
                )
            )
        );

        // Chase the Skies
        this.dropSelf(ModBlocks.DRIED_GHAST.get());

        // Spring to Life
        this.dropSelf(ModBlocks.FIREFLY_BUSH.get());
        this.add(ModBlocks.BUSH.get(), this::createShearsOrSilkTouchOnlyDrop);
        this.add(ModBlocks.WILDFLOWERS.get(), this.createPetalsDrops(ModBlocks.WILDFLOWERS.get()));
        this.add(ModBlocks.LEAF_LITTER.get(), this.createSegmentedBlockDrops(ModBlocks.LEAF_LITTER.get()));
        this.dropSelf(ModBlocks.CACTUS_FLOWER.get());
        this.add(ModBlocks.SHORT_DRY_GRASS.get(), this::createShearsOrSilkTouchOnlyDrop);
        this.add(ModBlocks.TALL_DRY_GRASS.get(), this::createShearsOrSilkTouchOnlyDrop);

        // Bats and Pots
        this.add(Blocks.DECORATED_POT, this::createDecoratedPotTable);

        // Chaos Cubed
        this.dropSelf(ModBlocks.SULFUR_SPIKE.get());
        this.dropSelf(ModBlocks.SULFUR.get());
        this.dropSelf(ModBlocks.POTENT_SULFUR.get());
        this.dropSelf(ModBlocks.SULFUR_STAIRS.get());
        this.add(ModBlocks.SULFUR_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.SULFUR_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_SULFUR.get());
        this.dropSelf(ModBlocks.POLISHED_SULFUR.get());
        this.dropSelf(ModBlocks.POLISHED_SULFUR_STAIRS.get());
        this.add(ModBlocks.POLISHED_SULFUR_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.POLISHED_SULFUR_WALL.get());
        this.dropSelf(ModBlocks.SULFUR_BRICKS.get());
        this.dropSelf(ModBlocks.SULFUR_BRICK_STAIRS.get());
        this.add(ModBlocks.SULFUR_BRICK_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.SULFUR_BRICK_WALL.get());
        this.dropSelf(ModBlocks.CINNABAR.get());
        this.dropSelf(ModBlocks.CINNABAR_STAIRS.get());
        this.add(ModBlocks.CINNABAR_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.CINNABAR_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_CINNABAR.get());
        this.dropSelf(ModBlocks.POLISHED_CINNABAR.get());
        this.dropSelf(ModBlocks.POLISHED_CINNABAR_STAIRS.get());
        this.add(ModBlocks.POLISHED_CINNABAR_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.POLISHED_CINNABAR_WALL.get());
        this.dropSelf(ModBlocks.CINNABAR_BRICKS.get());
        this.dropSelf(ModBlocks.CINNABAR_BRICK_STAIRS.get());
        this.add(ModBlocks.CINNABAR_BRICK_SLAB.get(), this::createSlabItemTable);
        this.dropSelf(ModBlocks.CINNABAR_BRICK_WALL.get());
        
        // Copper Age
        this.add(ModBlocks.COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.EXPOSED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.WEATHERED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.OXIDIZED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        
        this.add(ModBlocks.WAXED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        this.add(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get(), this::createNameableBlockEntityTable);
        
        this.dropSelf(ModBlocks.ACACIA_SHELF.get());
        this.dropSelf(ModBlocks.BAMBOO_SHELF.get());
        this.dropSelf(ModBlocks.BIRCH_SHELF.get());
        this.dropSelf(ModBlocks.CHERRY_SHELF.get());
        this.dropSelf(ModBlocks.CRIMSON_SHELF.get());
        this.dropSelf(ModBlocks.DARK_OAK_SHELF.get());
        this.dropSelf(ModBlocks.JUNGLE_SHELF.get());
        this.dropSelf(ModBlocks.MANGROVE_SHELF.get());
        this.dropSelf(ModBlocks.OAK_SHELF.get());
        this.dropSelf(ModBlocks.PALE_OAK_SHELF.get());
        this.dropSelf(ModBlocks.SPRUCE_SHELF.get());
        this.dropSelf(ModBlocks.WARPED_SHELF.get());
        
        this.add(ModBlocks.COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.EXPOSED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.WEATHERED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        
        this.add(ModBlocks.WAXED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        this.add(ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE.get(), this::createCopperGolemStatueBlock);
        
        this.dropSelf(ModBlocks.EXPOSED_LIGHTNING_ROD.get());
        this.dropSelf(ModBlocks.WEATHERED_LIGHTNING_ROD.get());
        this.dropSelf(ModBlocks.OXIDIZED_LIGHTNING_ROD.get());
        
        this.dropSelf(ModBlocks.WAXED_LIGHTNING_ROD.get());
        this.dropSelf(ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
        this.dropSelf(ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
        this.dropSelf(ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
        
        this.dropSelf(ModBlocks.COPPER_TORCH.getFirst().get());
        ModBlocks.COPPER_LANTERN.forEach(holder -> this.add(holder.get(), this::createSingleItemTable));
        ModBlocks.COPPER_BARS.forEach(holder -> this.dropSelf(holder.get()));
        ModBlocks.COPPER_CHAIN.forEach(holder -> this.dropSelf(holder.get()));
        
        // Miscellaneous
        this.dropSelf(ModBlocks.WHITE_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.ORANGE_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.MAGENTA_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.LIGHT_BLUE_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.YELLOW_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.LIME_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.PINK_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.GRAY_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.LIGHT_GRAY_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.CYAN_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.PURPLE_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.BLUE_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.BROWN_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.GREEN_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.RED_WOOL_STAIRS.get());
        this.dropSelf(ModBlocks.BLACK_WOOL_STAIRS.get());
        
        this.dropSelf(ModBlocks.WHITE_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.ORANGE_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.MAGENTA_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.LIGHT_BLUE_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.YELLOW_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.LIME_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.PINK_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.GRAY_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.LIGHT_GRAY_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.CYAN_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.PURPLE_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.BLUE_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.BROWN_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.GREEN_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.RED_WOOL_SLAB.get());
        this.dropSelf(ModBlocks.BLACK_WOOL_SLAB.get());

        this.dropSelf(ModBlocks.WHITE_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.ORANGE_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.MAGENTA_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.LIGHT_BLUE_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.YELLOW_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.LIME_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.PINK_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.GRAY_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.LIGHT_GRAY_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.CYAN_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.PURPLE_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.BLUE_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.BROWN_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.GREEN_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.RED_CONCRETE_STAIRS.get());
        this.dropSelf(ModBlocks.BLACK_CONCRETE_STAIRS.get());

        this.dropSelf(ModBlocks.WHITE_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.ORANGE_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.MAGENTA_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.LIGHT_BLUE_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.YELLOW_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.LIME_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.PINK_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.GRAY_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.LIGHT_GRAY_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.CYAN_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.PURPLE_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.BLUE_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.BROWN_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.GREEN_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.RED_CONCRETE_SLAB.get());
        this.dropSelf(ModBlocks.BLACK_CONCRETE_SLAB.get());
    }

    protected LootTable.Builder createMultifaceBlockDrops(Block block) {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .add(
                        this.applyExplosionDecay(
                            block,
                            LootItem.lootTableItem(block)
                                .apply(
                                    Direction.values(),
                                    direction -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true)
                                        .when(
                                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(direction), true))
                                        )
                                )
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true))
                        )
                    )
            );
    }

    protected LootTable.Builder createMossyCarpetBlockDrops(Block block) {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .add(
                        this.applyExplosionDecay(
                            block,
                            LootItem.lootTableItem(block)
                                .when(
                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MossyCarpetBlock.BASE, true))
                                )
                        )
                    )
            );
    }

    protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike itemLike) {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS_OR_SILK_TOUCH).add(LootItem.lootTableItem(itemLike)));
    }

    public LootTable.Builder createSegmentedBlockDrops(Block block) {
        return block instanceof SegmentableBlock segmentable
            ? LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(this.applyExplosionDecay(block,
                    LootItem.lootTableItem(block)
                        .apply(IntStream.rangeClosed(1, 4).boxed().toList(),
                            value -> SetItemCountFunction.setCount(ConstantValue.exactly((float) value))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(segmentable.getSegmentAmountProperty(), value)))))))
            : noDrop();
    }

    protected LootTable.Builder createDecoratedPotTable(Block pBlock) {
        return LootTable.lootTable()
            .withPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(
                        DynamicLoot.dynamicEntry(DecoratedPotBlock.SHERDS_DYNAMIC_DROP_ID)
                            .when(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.CRACKED, true))
                            )
                            .otherwise(
                                LootItem.lootTableItem(pBlock)
                                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy(DecoratedPotBlockEntity.TAG_SHERDS, "BlockEntityTag.sherds")
                                    )
                            )
                    )
            );
    }
    
    public LootTable.Builder createCopperGolemStatueBlock(Block block) {
        return LootTable.lootTable()
            .withPool(
                this.applyExplosionCondition(
                    block,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                            LootItem.lootTableItem(block)
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(CopyBlockState.copyState(block).copy(CopperGolemStatueBlock.POSE))
                        )
                )
            );
    }
}