package com.blackgear.vanillabackport.common;

import com.blackgear.platform.common.data.LootModifier;
import com.blackgear.platform.common.integration.BlockIntegration;
import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.platform.common.integration.TradeIntegration;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.interactions.mob.GhastHarnessInteraction;
import com.blackgear.vanillabackport.common.api.interactions.mob.ShearEquipmentInteraction;
import com.blackgear.vanillabackport.common.api.interactions.mob.LeashIntegration;
import com.blackgear.vanillabackport.common.api.interactions.mob.WolfArmorInteraction;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.common.level.dispenser.PaleOakBoatDispenseBehavior;
import com.blackgear.vanillabackport.common.level.entities.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.common.resource.sound.WolfSoundVariantReloadListener;
import com.blackgear.vanillabackport.common.resource.variants.*;
import com.blackgear.vanillabackport.common.worldgen.BiomeGeneration;
import com.blackgear.vanillabackport.common.worldgen.WorldGeneration;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.VillagerTrades.ItemsForEmeralds;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class CommonSetup {
    public static void setup() {
        ResourceReloadManager.registerServer(event -> {
            event.register(VanillaBackport.resource("wolf_sound_variants"), WolfSoundVariantReloadListener.INSTANCE);
            event.register(VanillaBackport.resource("cow_variants"), new CowVariantReloadListener());
            event.register(VanillaBackport.resource("chicken_variants"), new ChickenVariantReloadListener());
            event.register(VanillaBackport.resource("pig_variants"), new PigVariantReloadListener());
            event.register(VanillaBackport.resource("wolf_variants"), new WolfVariantReloadListener());
            event.register(VanillaBackport.resource("frog_variants"), new FrogVariantReloadListener());
            event.register(VanillaBackport.resource("cat_variants"), new CatVariantReloadListener());
        });

    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            BiomeManager.add(WorldGeneration::bootstrap);
            BiomePlacement.registerBiomePlacements(BiomeGeneration::bootstrap);
            MobIntegration.registerIntegrations(CommonSetup::mobIntegrations);
            BlockIntegration.registerIntegrations(CommonSetup::blockIntegrations);
            TradeIntegration.registerVillagerTrades(CommonSetup::tradeIntegrations);
            Parrot.MOB_SOUND_MAP.put(ModEntities.CREAKING.get(), ModSoundEvents.PARROT_IMITATE_CREAKING.get());
        });

        LootModifier.modify(new LootIntegrations());
    }

    public static void blockIntegrations(BlockIntegration.Event event) {
        event.registerFuelItem(ModBlocks.SHORT_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.TALL_DRY_GRASS.get(), 100);
        event.registerFuelItem(ModBlocks.LEAF_LITTER.get(), 100);

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
        event.registerFlammableBlock(ModBlocks.SHORT_DRY_GRASS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.TALL_DRY_GRASS.get(), 60, 100);
        event.registerFlammableBlock(ModBlocks.LEAF_LITTER.get(), 60, 100);

        event.registerCompostableItem(ModBlocks.PALE_OAK_LEAVES.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_OAK_SAPLING.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_CARPET.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_HANGING_MOSS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.PALE_MOSS_BLOCK.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.LEAF_LITTER.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.FIREFLY_BUSH.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.WILDFLOWERS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.CACTUS_FLOWER.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.SHORT_DRY_GRASS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.TALL_DRY_GRASS.get(), 0.3F);
        event.registerCompostableItem(ModBlocks.OPEN_EYEBLOSSOM.get(), 0.65F);
        event.registerCompostableItem(ModBlocks.CLOSED_EYEBLOSSOM.get(), 0.65F);

        event.registerStrippableBlock(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get());
        event.registerStrippableBlock(ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());

        event.registerDispenserBehavior(ModItems.PALE_OAK_BOAT.get(), new PaleOakBoatDispenseBehavior());
        event.registerDispenserBehavior(ModItems.PALE_OAK_CHEST_BOAT.get(), new PaleOakBoatDispenseBehavior(true));
        event.registerDispenserBehavior(ModItems.BLUE_EGG.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                ThrownEgg thrownEgg = new ThrownEgg(level, position.x(), position.y(), position.z());
                VariantDataHolder.<ChickenVariant>getHolder(thrownEgg).setVariantData(VariantUtils.getDefault(ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.COLD));
                return Util.make(thrownEgg, egg -> egg.setItem(stack));
            }
        });
        event.registerDispenserBehavior(ModItems.BROWN_EGG.get(), new AbstractProjectileDispenseBehavior() {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                ThrownEgg thrownEgg = new ThrownEgg(level, position.x(), position.y(), position.z());
                VariantDataHolder.<ChickenVariant>getHolder(thrownEgg).setVariantData(VariantUtils.getDefault(ModBuiltinRegistries.CHICKEN_VARIANTS, ChickenVariants.WARM));
                return Util.make(thrownEgg, egg -> egg.setItem(stack));
            }
        });
    }

    public static void tradeIntegrations(TradeIntegration.Event event) {
        if (VanillaBackport.COMMON_CONFIG.hasPaleTrades.get()) {
            event.registerWandererTrade(
                false,
                new ItemsForEmeralds(ModBlocks.PALE_OAK_LOG.get(), 1, 8, 4, 1)
            );
            event.registerWandererTrade(
                true,
                new ItemsForEmeralds(ModBlocks.OPEN_EYEBLOSSOM.get(), 1, 1, 7, 1),
                new ItemsForEmeralds(ModBlocks.PALE_OAK_SAPLING.get(), 5, 1, 8, 1),
                new ItemsForEmeralds(ModBlocks.PALE_HANGING_MOSS.get(), 1, 3, 4, 1),
                new ItemsForEmeralds(ModBlocks.PALE_MOSS_BLOCK.get(), 1, 2, 5, 1)
            );
        }

        if (VanillaBackport.COMMON_CONFIG.hasSpringTrades.get()) {
            event.registerWandererTrade(
                true,
                new ItemsForEmeralds(ModBlocks.WILDFLOWERS.get(), 1, 1, 12, 1),
                new ItemsForEmeralds(ModBlocks.TALL_DRY_GRASS.get(), 1, 1, 12, 1),
                new ItemsForEmeralds(ModBlocks.FIREFLY_BUSH.get(), 3, 1, 12, 1)
            );
        }
    }

    public static void mobIntegrations(MobIntegration.Event event) {
        event.registerMobInteraction(new LeashIntegration());
        event.registerMobInteraction(new ShearEquipmentInteraction());
        event.registerMobInteraction(new WolfArmorInteraction());
        event.registerMobInteraction(new GhastHarnessInteraction());

        event.registerPlacement(ModEntities.ARMADILLO, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, reason, pos, random) -> level.getBlockState(pos.below()).is(ModBlockTags.ARMADILLO_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8);
        event.registerPlacement(() -> EntityType.CAMEL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, reason, pos, random) -> level.getBlockState(pos.below()).is(ModBlockTags.CAMELS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8);

        event.registerAttributes(ModEntities.ARMADILLO, Armadillo::createAttributes);
        event.registerAttributes(ModEntities.CREAKING, Creaking::createAttributes);
        event.registerAttributes(ModEntities.HAPPY_GHAST, HappyGhast::createAttributes);

        event.registerGoal(EntityType.VINDICATOR, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.PILLAGER, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.ILLUSIONER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.EVOKER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));

        event.registerGoal(mob -> mob instanceof Spider, 2, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Armadillo.class, 6.0F, 1.0, 1.2, entity -> !((Armadillo) entity).isScared()));
    }
}
