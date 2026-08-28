package com.blackgear.vanillabackport.data.server.advancement;

import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.triggers.SpearMobsTrigger;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        // Adventure advancements
        AdvancementHolder adventure = Advancement.Builder.advancement().build(ResourceLocation.withDefaultNamespace("adventure/root"));

        Advancement.Builder.advancement()
            .parent(adventure)
            .display(
                ModBlocks.CREAKING_HEART.get(),
                Component.translatable("advancements.adventure.heart_transplanter.title"),
                Component.translatable("advancements.adventure.heart_transplanter.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(
                "place_creaking_heart_dormant",
                placedBlockWithProperties(ModBlocks.CREAKING_HEART.get(), ModBlockStateProperties.CREAKING_HEART_STATE, CreakingHeartState.DORMANT)
            )
            .addCriterion(
                "place_creaking_heart_awake",
                placedBlockWithProperties(ModBlocks.CREAKING_HEART.get(), ModBlockStateProperties.CREAKING_HEART_STATE, CreakingHeartState.AWAKE)
            )
            .addCriterion(
                "place_pale_oak_log", placedBlockActivatesCreakingHeart(ModBlockTags.PALE_OAK_LOGS)
            )
            .save(consumer, "adventure/heart_transplanter");
        
        AdvancementHolder killAMob = Advancement.Builder.advancement().build(ResourceLocation.withDefaultNamespace("adventure/kill_a_mob"));
        Advancement.Builder.advancement()
            .parent(killAMob)
            .display(
                ModItems.IRON_SPEAR.get(),
                Component.translatable("advancements.adventure.spear_many_mobs.title"),
                Component.translatable("advancements.adventure.spear_many_mobs.description"),
                null,
                AdvancementType.GOAL,
                true,
                true,
                false
            )
            .addCriterion("spear_many_mobs", SpearMobsTrigger.TriggerInstance.spearMobs(5))
            .save(consumer, "adventure/spear_many_mobs");

        // Husbandry advancements
        AdvancementHolder husbandry = Advancement.Builder.advancement().build(ResourceLocation.withDefaultNamespace("husbandry/root"));

        Advancement.Builder.advancement()
            .parent(husbandry)
            .display(
                ModBlocks.DRIED_GHAST.get(),
                Component.translatable("advancements.husbandry.place_dried_ghast_in_water.title"),
                Component.translatable("advancements.husbandry.place_dried_ghast_in_water.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .addCriterion(
                "place_dried_ghast_on_water",
                placedBlockWithProperties(ModBlocks.DRIED_GHAST.get(), BlockStateProperties.WATERLOGGED, String.valueOf(true))
            )
            .save(consumer, "husbandry/place_dried_ghast_in_water");


        Advancement.Builder.advancement()
            .parent(husbandry)
            .display(
                Items.TNT,
                Component.translatable("advancements.husbandry.uh_oh.title"),
                Component.translatable("advancements.husbandry.uh_oh.description"),
                null,
                AdvancementType.TASK,
                true,
                true,
                false
            )
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(
                "pick_up_dropped_tnt",
                PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(
                    ContextAwarePredicate.create(),
                    Optional.of(ItemPredicate.Builder.item().of(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE).build()),
                    Optional.of(EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(ModEntityTypes.SULFUR_CUBE.get())
                            .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))
                            .build()
                    ))
                )
            )
            .addCriterion(
                "give_tnt_directly",
                PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                    Optional.empty(),
                    ItemPredicate.Builder.item().of(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE),
                    Optional.of(EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(ModEntityTypes.SULFUR_CUBE.get())
                            .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false))
                            .build()
                    ))
                )
            ).save(consumer, "husbandry/uh_oh");
    }

    private Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockActivatesCreakingHeart(TagKey<Block> block) {
        LootItemCondition.Builder[] conditions = Stream.of(Direction.values()).map(direction -> {
            StatePropertiesPredicate.Builder creakingHeartProperties = StatePropertiesPredicate.Builder.properties().hasProperty(CreakingHeartBlock.AXIS, direction.getAxis());
            BlockPredicate.Builder placedPaleOakLogBlock = BlockPredicate.Builder.block().of(block).setProperties(creakingHeartProperties);
            Vec3i blockOffset = direction.getNormal();
            LootItemCondition.Builder placedPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock));
            LootItemCondition.Builder creakingHeartBlockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(ModBlocks.CREAKING_HEART.get()).setProperties(creakingHeartProperties)), new BlockPos(blockOffset));
            LootItemCondition.Builder existingPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock), new BlockPos(blockOffset.multiply(2)));
            return AllOfCondition.allOf(placedPaleOakLogTest, creakingHeartBlockTest, existingPaleOakLogTest);
        }).toArray(LootItemCondition.Builder[]::new);
        return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(conditions));
    }

    private static <T extends Comparable<T>> Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockWithProperties(Block block, Property<T> property, String value) {
        StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties().hasProperty(property, value);
        ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemBlockStatePropertyCondition.Builder(block).setProperties(builder).build());
        return CriteriaTriggers.PLACED_BLOCK.createCriterion(new ItemUsedOnLocationTrigger.TriggerInstance(Optional.empty(), Optional.of(location)));
    }

    private static <T extends Comparable<T> & StringRepresentable> Criterion<ItemUsedOnLocationTrigger.TriggerInstance> placedBlockWithProperties(Block block, Property<T> property, T value) {
        return placedBlockWithProperties(block, property, value.getSerializedName());
    }
}