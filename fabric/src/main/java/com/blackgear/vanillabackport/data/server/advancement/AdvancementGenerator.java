package com.blackgear.vanillabackport.data.server.advancement;

import com.blackgear.vanillabackport.common.criterion.PlayerShearedEquipmentTrigger;
import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.blocks.states.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.ModBlockStateProperties;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.predicates.*;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class AdvancementGenerator extends FabricAdvancementProvider {
    public AdvancementGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // Adventure advancements
        Advancement adventure = Advancement.Builder.advancement().build(new ResourceLocation("adventure/root"));

        Advancement.Builder.advancement()
            .parent(adventure)
            .display(
                ModBlocks.CREAKING_HEART.get(),
                Component.translatable("advancements.adventure.heart_transplanter.title"),
                Component.translatable("advancements.adventure.heart_transplanter.description"),
                null,
                FrameType.TASK,
                true,
                true,
                false
            )
            .requirements(RequirementsStrategy.OR)
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


        // Husbandry advancements
        Advancement husbandry = Advancement.Builder.advancement().build(new ResourceLocation("husbandry/root"));

        Advancement tameAnAnimal = Advancement.Builder.advancement().build(new ResourceLocation("husbandry/tame_an_animal"));

        addWolfVariants(Advancement.Builder.advancement())
            .parent(tameAnAnimal)
            .display(
                Items.BONE,
                Component.translatable("advancements.husbandry.whole_pack.title"),
                Component.translatable("advancements.husbandry.whole_pack.description"),
                null,
                FrameType.CHALLENGE,
                true,
                true,
                false
            )
            .rewards(AdvancementRewards.Builder.experience(50))
            .save(consumer, "husbandry/whole_pack");

        Advancement.Builder.advancement()
            .parent(tameAnAnimal)
            .display(
                Items.SHEARS,
                Component.translatable("advancements.husbandry.remove_wolf_armor.title"),
                Component.translatable("advancements.husbandry.remove_wolf_armor.description"),
                null,
                FrameType.TASK,
                true,
                true,
                false
            )
            .addCriterion(
                "remove_wolf_armor",
                PlayerShearedEquipmentTrigger.TriggerInstance.equipmentSheared(
                    ItemPredicate.Builder.item().of(ModItems.WOLF_ARMOR.get()),
                    EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.WOLF).build())
                )
            )
            .save(consumer, "husbandry/remove_wolf_armor");

        Advancement.Builder.advancement()
            .parent(tameAnAnimal)
            .display(
                ModItems.WOLF_ARMOR.get(),
                Component.translatable("advancements.husbandry.repair_wolf_armor.title"),
                Component.translatable("advancements.husbandry.repair_wolf_armor.description"),
                null,
                FrameType.TASK,
                true,
                true,
                false
            )
            .addCriterion(
                "repair_wolf_armor",
                PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                    ItemPredicate.Builder.item().of(ModItems.ARMADILLO_SCUTE.get()),
                    EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(EntityType.WOLF)
                            .equipment(
                                EntityEquipmentPredicate.Builder.equipment()
                                    .chest(
                                        ItemPredicate.Builder.item()
                                            .of(ModItems.WOLF_ARMOR.get())
                                            .hasNbt(getNbt(tag -> tag.putInt("Damage", 0)))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                )
            )
            .save(consumer, "husbandry/repair_wolf_armor");

        Advancement.Builder.advancement()
            .parent(husbandry)
            .display(
                ModBlocks.DRIED_GHAST.get(),
                Component.translatable("advancements.husbandry.place_dried_ghast_in_water.title"),
                Component.translatable("advancements.husbandry.place_dried_ghast_in_water.description"),
                null,
                FrameType.TASK,
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
                FrameType.TASK,
                true,
                true,
                false
            )
            .requirements(RequirementsStrategy.OR)
            .addCriterion(
                "pick_up_dropped_tnt",
                PickedUpItemTrigger.TriggerInstance.thrownItemPickedUpByEntity(
                    ContextAwarePredicate.ANY,
                    ItemPredicate.Builder.item().of(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE).build(),
                    EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(ModEntityTypes.SULFUR_CUBE)
                            .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build())
                            .build()
                    )
                )
            )
            .addCriterion(
                "give_tnt_directly",
                PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                    ContextAwarePredicate.ANY,
                    ItemPredicate.Builder.item().of(ModItemTags.SULFUR_CUBE_ARCHETYPE_EXPLOSIVE),
                    EntityPredicate.wrap(
                        EntityPredicate.Builder.entity()
                            .of(ModEntityTypes.SULFUR_CUBE)
                            .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(false).build())
                            .build()
                    )
                )
            ).save(consumer, "husbandry/uh_oh");
    }

    private static CompoundTag getNbt(Consumer<CompoundTag> consumer) {
        CompoundTag tag = new CompoundTag();
        consumer.accept(tag);
        return tag;
    }

    private static Advancement.Builder addWolfVariants(Advancement.Builder builder) {
        ModBuiltinRegistries.WOLF_VARIANTS.entries()
            .forEach((key, value) -> {
                CompoundTag nbt = getNbt(tag -> tag.putString("variant", key.toString()));
                builder.addCriterion(
                    key.toString(),
                    TameAnimalTrigger.TriggerInstance.tamedAnimal(
                        EntityPredicate.Builder.entity().of(EntityType.WOLF).nbt(new NbtPredicate(nbt)).build()
                    )
                );
            });
        return builder;
    }

    private ItemUsedOnLocationTrigger.TriggerInstance placedBlockActivatesCreakingHeart(TagKey<Block> block) {
        LootItemCondition.Builder[] conditions = Stream.of(Direction.values()).map(direction -> {
            StatePropertiesPredicate.Builder creakingHeartProperties = StatePropertiesPredicate.Builder.properties().hasProperty(CreakingHeartBlock.AXIS, direction.getAxis());
            BlockPredicate.Builder placedPaleOakLogBlock = BlockPredicate.Builder.block().of(block).setProperties(creakingHeartProperties.build());
            Vec3i blockOffset = direction.getNormal();
            LootItemCondition.Builder placedPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock.build()));
            LootItemCondition.Builder creakingHeartBlockTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(ModBlocks.CREAKING_HEART.get()).setProperties(creakingHeartProperties.build()).build()), new BlockPos(blockOffset));
            LootItemCondition.Builder existingPaleOakLogTest = LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(placedPaleOakLogBlock.build()), new BlockPos(blockOffset.multiply(2)));
            return AllOfCondition.allOf(placedPaleOakLogTest, creakingHeartBlockTest, existingPaleOakLogTest);
        }).toArray(LootItemCondition.Builder[]::new);
        return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AnyOfCondition.anyOf(conditions));
    }

    private static <T extends Comparable<T>> ItemUsedOnLocationTrigger.TriggerInstance placedBlockWithProperties(Block block, Property<T> property, String value) {
        StatePropertiesPredicate.Builder builder = StatePropertiesPredicate.Builder.properties().hasProperty(property, value);
        ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemBlockStatePropertyCondition.Builder(block).setProperties(builder).build());
        return new ItemUsedOnLocationTrigger.TriggerInstance(CriteriaTriggers.PLACED_BLOCK.getId(), ContextAwarePredicate.ANY, location);
    }

    private static <T extends Comparable<T> & StringRepresentable> ItemUsedOnLocationTrigger.TriggerInstance placedBlockWithProperties(Block block, Property<T> property, T value) {
        return placedBlockWithProperties(block, property, value.getSerializedName());
    }
}