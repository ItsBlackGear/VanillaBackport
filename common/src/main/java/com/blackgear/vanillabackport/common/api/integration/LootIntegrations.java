package com.blackgear.vanillabackport.common.api.integration;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.core.events.ServerLifecycleEvents;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

public class LootIntegrations implements LootModifier.LootTableModifier {
    private static final List<ResourceKey<LootTable>> CONTAIN_BUNDLE = List.of(
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
    public void modify(ResourceKey<LootTable> key, LootModifier.LootTableContext context, boolean builtin) {
        // GHASTS DROP TEARS MUSIC DISC
        if (key.equals(EntityType.GHAST.getDefaultLootTable()) && VanillaBackport.COMMON_CONFIG.hasTearsMusicDisc.get()) {
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
        if (key.equals(BuiltInLootTables.PIGLIN_BARTERING) && VanillaBackport.COMMON_CONFIG.hasDriedGhasts.get()) {
            context.addToPool(
                LootItem.lootTableItem(ModBlocks.DRIED_GHAST.get())
                    .setWeight(10)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                    .build()
            );
        }

        // CHICKEN JOCKEYS DROP LAVA CHICKEN MUSIC DISC
        if (key.equals(EntityType.ZOMBIE.getDefaultLootTable()) && VanillaBackport.COMMON_CONFIG.hasLavaChickenMusicDisc.get()) {
            context.addPool(
                LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.MUSIC_DISC_LAVA_CHICKEN.get()))
                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                    .when(
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.THIS,
                            EntityPredicate.Builder.entity()
                                .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true))
                                .vehicle(EntityPredicate.Builder.entity().of(EntityType.CHICKEN))
                        )
                    )
            );
        }

        // GENERATE RESIN CLUMPS IN WOODLAND MANSION CHESTS
        if (key.equals(BuiltInLootTables.WOODLAND_MANSION) && VanillaBackport.COMMON_CONFIG.hasResinLoot.get()) {
            context.addToPool(
                1,
                LootItem.lootTableItem(ModBlocks.RESIN_CLUMP.get())
                    .setWeight(50)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                    .build()
            );
        }

        // GENERATE BUNDLES IN VILLAGE CHESTS
        if (CONTAIN_BUNDLE.contains(key) && VanillaBackport.COMMON_CONFIG.hasBundleLoot.get()) {
            context.addPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                    .add(EmptyLootItem.emptyItem().setWeight(2))
            );
        }

        // GENERATE LODESTONE IN RUINED PORTAL CHESTS
        if (key.equals(BuiltInLootTables.RUINED_PORTAL) && VanillaBackport.COMMON_CONFIG.hasLodestoneLoot.get()) {
            context.addPool(
                LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(Items.LODESTONE).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                    .add(EmptyLootItem.emptyItem().setWeight(1))
            );
        }

        // GENERATE BOUNCE MUSIC DISC ON SULFUR CAVE MINESHAFTS
        if (key.equals(BuiltInLootTables.ABANDONED_MINESHAFT) && VanillaBackport.COMMON_CONFIG.hasBounceMusicDisc.get()) {
            ServerLifecycleEvents.STARTING.register(server -> {
                var lookup = server.registryAccess().lookupOrThrow(Registries.BIOME);
                context.addToPool(
                    2,
                    LootItem.lootTableItem(ModItems.MUSIC_DISC_BOUNCE.get())
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(lookup.getOrThrow(ModBiomes.SULFUR_CAVES)))))
                        .setWeight(10)
                        .build()
                );
            });
        }
    }
}