package com.blackgear.vanillabackport.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

public class LootIntegrations implements LootModifier.LootTableModifier {
    private static final List<ResourceLocation> CONTAIN_BUNDLE = List.of(
        BuiltInLootTables.VILLAGE_WEAPONSMITH,
        BuiltInLootTables.VILLAGE_CARTOGRAPHER,
        BuiltInLootTables.VILLAGE_TANNERY,
        BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
        BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
        BuiltInLootTables.VILLAGE_SAVANNA_HOUSE,
        BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
        BuiltInLootTables.VILLAGE_DESERT_HOUSE
    );

    @Override
    public void modify(ResourceLocation path, LootModifier.LootTableContext context, boolean builtin) {
        // GHASTS DROP TEARS MUSIC DISC
        if (path.equals(EntityType.GHAST.getDefaultLootTable()) && VanillaBackport.COMMON_CONFIG.hasTearsMusicDisc.get()) {
            context.addPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(ModItems.MUSIC_DISC_TEARS.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                    .when(DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                            .direct(EntityPredicate.Builder.entity().of(EntityType.FIREBALL)))
                    )
                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
            );
        }

        // PIGLINS BARTER DRIED GHASTS
        if (path.equals(BuiltInLootTables.PIGLIN_BARTERING) && VanillaBackport.COMMON_CONFIG.hasDriedGhasts.get()) {
            context.addToPool(
                LootItem.lootTableItem(ModBlocks.DRIED_GHAST.get())
                    .setWeight(10)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                    .build()
            );
        }

        // CHICKEN JOCKEYS DROP LAVA CHICKEN MUSIC DISC
        if (path.equals(EntityType.ZOMBIE.getDefaultLootTable())) {
            context.addPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.MUSIC_DISC_LAVA_CHICKEN.get()))
                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                    .when(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true).build())
                                .vehicle(EntityPredicate.Builder.entity().of(EntityType.CHICKEN).build())
                        )
                    )
            );
        }

        // RESIN ON WOODLAND MANSION CHESTS
        if (path.equals(BuiltInLootTables.WOODLAND_MANSION) && VanillaBackport.COMMON_CONFIG.hasResin.get()) {
            context.addToPool(
                1,
                LootItem.lootTableItem(ModBlocks.RESIN_CLUMP.get())
                    .setWeight(50)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                    .build()
            );
        }

        // VILLAGES GENERATE BUNDLES
        if (CONTAIN_BUNDLE.contains(path) && VanillaBackport.COMMON_CONFIG.hasVillageBundles.get()) {
            context.addPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                    .add(EmptyLootItem.emptyItem().setWeight(2))
            );
        }
    }
}
