package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltInLootTables;
import com.blackgear.vanillabackport.common.registries.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
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

import java.util.function.BiConsumer;

public class GiftLootGenerator extends SimpleFabricLootTableProvider {
    public GiftLootGenerator(FabricDataOutput output) {
        super(output, LootContextParamSets.GIFT);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(
            ModBuiltInLootTables.ARMADILLO_SHED,
            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.ARMADILLO_SCUTE.get())))
        );
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
                                EntityPredicate.Builder.entity().nbt(hasVariant(ChickenVariants.TEMPERATE))
                            )
                        ),
                        LootItem.lootTableItem(ModItems.BROWN_EGG.get())
                        .when(
                            LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().nbt(hasVariant(ChickenVariants.WARM))
                            )
                        ),
                        LootItem.lootTableItem(ModItems.BLUE_EGG.get())
                        .when(
                            LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity().nbt(hasVariant(ChickenVariants.COLD))
                            )
                        )
                    )
                )
            )
        );
    }

    private static NbtPredicate hasVariant(ResourceKey<ChickenVariant> key) {
        CompoundTag tag = new CompoundTag();
        tag.putString("variant", key.location().toString());
        return new NbtPredicate(tag);
    }
}