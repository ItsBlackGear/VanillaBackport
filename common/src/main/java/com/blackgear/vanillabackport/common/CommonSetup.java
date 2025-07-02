package com.blackgear.vanillabackport.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.integration.TradeIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.vanillabackport.common.api.leash.LeashIntegration;
import com.blackgear.vanillabackport.common.level.dispenser.PaleOakBoatDispenseBehavior;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.common.worldgen.BiomeGeneration;
import com.blackgear.vanillabackport.common.worldgen.WorldGeneration;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemsForEmeralds;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class CommonSetup {
    public static void setup() {
        MobIntegration.registerIntegrations(CommonSetup::mobIntegrations);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            BiomeManager.add(WorldGeneration::bootstrap);
            BiomePlacement.registerBiomePlacements(BiomeGeneration::bootstrap);
            BlockIntegration.registerIntegrations(CommonSetup::blockIntegrations);
            TradeIntegration.registerVillagerTrades(CommonSetup::tradeIntegrations);
        });

        LootModifier.modify(CommonSetup::lootIntegrations);
    }

    public static void blockIntegrations(BlockIntegration.Event event) {
        event.registerFlammableBlock(ModBlocks.PALE_OAK_PLANKS.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_SLAB.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_FENCE_GATE.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_FENCE.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_STAIRS.get(), 5, 20);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_LOG.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_WOOD.get(), 5, 5);
        event.registerFlammableBlock(ModBlocks.PALE_OAK_LEAVES.get(), 30, 60);
        event.registerFlammableBlock(ModBlocks.PALE_MOSS_BLOCK.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.PALE_MOSS_CARPET.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.PALE_HANGING_MOSS.get(), 5, 100);
        event.registerFlammableBlock(ModBlocks.OPEN_EYEBLOSSOM.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.CLOSED_EYEBLOSSOM.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.BUSH.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.FIREFLY_BUSH.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.WILDFLOWERS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.CACTUS_FLOWER.get(), 60, 100);

        event.registerCompostableItem(ModBlocks.PALE_OAK_LEAVES.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_OAK_SAPLING.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_CARPET.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_HANGING_MOSS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_BLOCK.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.FIREFLY_BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.WILDFLOWERS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.CACTUS_FLOWER.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.OPEN_EYEBLOSSOM.get(), 0.65F);
        event.registerCompostableItem(ModBlocks.CLOSED_EYEBLOSSOM.get(), 0.65F);

        event.registerStrippableBlock(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        event.registerStrippableBlock(ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());

        event.registerDispenserBehavior(ModItems.PALE_OAK_BOAT.get(), new PaleOakBoatDispenseBehavior());
        event.registerDispenserBehavior(ModItems.PALE_OAK_CHEST_BOAT.get(), new PaleOakBoatDispenseBehavior(true));
    }

    public static void tradeIntegrations(TradeIntegration.Event event) {
        if (VanillaBackport.CONFIG.paleTradesFromWanderer.get()) {
            event.registerWandererTrade(
                false,
                new ItemsForEmeralds(ModBlocks.PALE_OAK_LOG.get(), 1, 8, 4, 1)
            );
            event.registerWandererTrade(
                true,
                new ItemsForEmeralds(ModBlocks.PALE_OAK_SAPLING.get(), 5, 1, 8, 1),
                new ItemsForEmeralds(ModBlocks.PALE_HANGING_MOSS.get(), 1, 3, 4, 1),
                new ItemsForEmeralds(ModBlocks.PALE_MOSS_BLOCK.get(), 1, 2, 5, 1),
                new ItemsForEmeralds(ModBlocks.OPEN_EYEBLOSSOM.get(), 1, 1, 7, 1),
                new ItemsForEmeralds(ModBlocks.FIREFLY_BUSH.get(), 3, 1, 12, 1),
                new ItemsForEmeralds(ModBlocks.WILDFLOWERS.get(), 1, 1, 12, 1)
            );
        }
    }

    public static void mobIntegrations(MobIntegration.Event event) {
        event.registerMobInteraction(new LeashIntegration());

        event.registerAttributes(ModEntities.CREAKING, Creaking::createAttributes);
        event.registerAttributes(ModEntities.HAPPY_GHAST, HappyGhast::createAttributes);

        event.registerGoal(EntityType.VINDICATOR, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.PILLAGER, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.ILLUSIONER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.EVOKER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
    }

    public static void lootIntegrations(ResourceLocation path, LootModifier.LootTableContext context, boolean builtin) {
        if (path.equals(EntityType.GHAST.getDefaultLootTable())) {
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

        if (path.equals(BuiltInLootTables.PIGLIN_BARTERING)) {
            context.addToPool(
                LootItem.lootTableItem(ModBlocks.DRIED_GHAST.get())
                    .setWeight(10)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                    .build()
            );
        }

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

        if (path.equals(BuiltInLootTables.WOODLAND_MANSION)) {
            context.addToPool(
                1,
                LootItem.lootTableItem(ModBlocks.RESIN_CLUMP.get())
                    .setWeight(50)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                    .build()
            );
        }
    }
}