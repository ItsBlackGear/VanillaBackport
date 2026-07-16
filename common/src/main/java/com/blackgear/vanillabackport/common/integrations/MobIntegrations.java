package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.platform.common.integration.MobIntegration;
import com.blackgear.vanillabackport.common.integrations.interactions.GhastHarnessInteraction;
import com.blackgear.vanillabackport.common.integrations.interactions.LeashInteraction;
import com.blackgear.vanillabackport.common.level.entity.ai.goal.OfferCopperGolemFlowerGoal;
import com.blackgear.vanillabackport.common.level.entity.ai.goal.SpearUseGoal;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.skeleton.Parched;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

public class MobIntegrations {
    private static void registerInteractions(MobIntegration.Event event) {
        event.registerMobInteraction(new GhastHarnessInteraction());
        event.registerMobInteraction(new LeashInteraction());
    }
    
    private static void registerPlacements(MobIntegration.Event event) {
        event.registerPlacement(() -> EntityType.CAMEL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (type, level, reason, pos, random) -> level.getBlockState(pos.below()).is(ModBlockTags.CAMELS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8);
        event.registerPlacement(ModEntityTypes.SULFUR_CUBE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SulfurCube::checkSulfurCubeSpawnRules);
        event.registerPlacement(ModEntityTypes.PARCHED, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MobIntegrations::checkSurfaceMonstersSpawnRules);
        event.registerPlacement(ModEntityTypes.CAMEL_HUSK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MobIntegrations::checkSurfaceMonstersSpawnRules);
    }
    
    private static void registerAttributes(MobIntegration.Event event) {
        event.registerAttributes(ModEntityTypes.CREAKING, Creaking::createAttributes);
        event.registerAttributes(ModEntityTypes.HAPPY_GHAST, HappyGhast::createAttributes);
        event.registerAttributes(ModEntityTypes.SULFUR_CUBE, SulfurCube::createSulfurCubeAttributes);
        event.registerAttributes(ModEntityTypes.COPPER_GOLEM, CopperGolem::createAttributes);
        event.registerAttributes(ModEntityTypes.PARCHED, Parched::createAttributes);
        event.registerAttributes(ModEntityTypes.CAMEL_HUSK, Camel::createAttributes);
    }
    
    private static void registerGoals(MobIntegration.Event event) {
        event.registerGoal(EntityType.VINDICATOR, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.PILLAGER, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.ILLUSIONER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.EVOKER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        
        event.registerGoal(EntityType.IRON_GOLEM, 5, mob -> new OfferCopperGolemFlowerGoal((IronGolem) mob));
        
        event.registerGoal(mob -> mob instanceof Zombie, 2, mob -> new SpearUseGoal<>((Monster) mob, 1.0, 1.0, 10.0F, 2.0F));
        event.registerGoal(mob -> mob instanceof ZombifiedPiglin, 1, mob -> new SpearUseGoal<>((Monster) mob, 1.0, 1.0, 10.0F, 2.0F));
    }
    
    public static void bootstrap(MobIntegration.Event event) {
        registerInteractions(event);
        registerPlacements(event);
        registerAttributes(event);
        registerGoals(event);
    }
    
    private static boolean checkMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && (MobSpawnType.ignoresLightRequirements(spawnType) || Monster.isDarkEnoughToSpawn(level, pos, random)) && Mob.checkMobSpawnRules(type, level, spawnType, pos, random);
    }
    
    private static boolean checkSurfaceMonstersSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(type, level, spawnType, pos, random) && (MobSpawnType.isSpawner(spawnType) || level.canSeeSky(pos));
    }
}