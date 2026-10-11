package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.data.ModBuiltInLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class GiftLootGenerator extends SimpleFabricLootTableProvider {
    public GiftLootGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.GIFT);
    }

    /**
     * Creates an NBT predicate that matches a chicken with the specified variant.
     * The variant is stored in the "variant" NBT tag as a resource location string.
     */
    private static NbtPredicate variantNbt(ResourceLocation variantId) {
        CompoundTag tag = new CompoundTag();
        tag.putString("variant", variantId.toString());
        return new NbtPredicate(tag);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

        output.accept(
            ModBuiltInLootTables.CHICKEN_LAY,
            LootTable.lootTable()
                .withPool(
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(
                            AlternativesEntry.alternatives(
                                LootItem.lootTableItem(Items.EGG)
                                    .when(
                                        LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity()
                                                .nbt(variantNbt(ChickenVariants.TEMPERATE.location()))
                                        )
                                    ),
                                LootItem.lootTableItem(ModItems.BROWN_EGG.get())
                                    .when(
                                        LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity()
                                                .nbt(variantNbt(ChickenVariants.WARM.location()))
                                        )
                                    ),
                                LootItem.lootTableItem(ModItems.BLUE_EGG.get())
                                    .when(
                                        LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity()
                                                .nbt(variantNbt(ChickenVariants.COLD.location()))
                                        )
                                    )
                            )
                        )
                )
        );
    }
}