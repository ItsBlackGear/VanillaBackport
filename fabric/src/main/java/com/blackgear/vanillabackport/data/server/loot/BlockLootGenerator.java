package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.level.blocks.LeafLitterBlock;
import com.blackgear.vanillabackport.common.level.blocks.MossyCarpetBlock;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
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
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
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
        this.add(ModBlocks.LEAF_LITTER.get(), this.createLeafLitterDrops(ModBlocks.LEAF_LITTER.get()));
        this.dropSelf(ModBlocks.CACTUS_FLOWER.get());
        this.add(ModBlocks.SHORT_DRY_GRASS.get(), this::createShearsOrSilkTouchOnlyDrop);
        this.add(ModBlocks.TALL_DRY_GRASS.get(), this::createShearsOrSilkTouchOnlyDrop);

        // Bats and Pots
        this.add(Blocks.DECORATED_POT, this::createDecoratedPotTable);
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

    public LootTable.Builder createLeafLitterDrops(Block petalBlock) {
        return LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(this.applyExplosionDecay(petalBlock,
                    LootItem.lootTableItem(petalBlock)
                        .apply(IntStream.rangeClosed(1, 4).boxed().toList(),
                            value -> SetItemCountFunction.setCount(ConstantValue.exactly((float) value))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(petalBlock)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LeafLitterBlock.AMOUNT, value)))))));
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
}