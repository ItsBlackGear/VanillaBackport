package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.platform.common.integration.v2.spawn_placement.SpawnPlacementStrategy;
import com.blackgear.vanillabackport.common.integrations.interactions.GhastHarnessInteraction;
import com.blackgear.vanillabackport.common.integrations.interactions.LeashIntegration;
import com.blackgear.vanillabackport.common.integrations.interactions.ShearEquipmentInteraction;
import com.blackgear.vanillabackport.common.integrations.interactions.WolfArmorInteraction;
import com.blackgear.vanillabackport.common.level.entities.ai.goal.OfferCopperGolemFlowerGoal;
import com.blackgear.vanillabackport.common.level.entities.ai.goal.SpearUseGoal;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.ZombieNautilus;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.skeleton.Parched;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

import static com.blackgear.platform.common.integration.MobIntegration.*;
import static net.minecraft.world.entity.SpawnPlacements.*;
import static net.minecraft.world.level.levelgen.Heightmap.*;

public class MobIntegrations {
    private static void registerInteractions(Event event) {
        event.registerMobInteraction(new LeashIntegration());
        event.registerMobInteraction(new ShearEquipmentInteraction());
        event.registerMobInteraction(new WolfArmorInteraction());
        event.registerMobInteraction(new GhastHarnessInteraction());
    }
    
    private static void registerPlacements(Event event) {
        event.registerPlacement(ModEntityTypes.ARMADILLO, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, Armadillo::checkArmadilloSpawnRules);
        event.registerPlacement(() -> EntityType.CAMEL, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, MobSpawns::checkCamelSpawnRules, SpawnPlacementStrategy.OR);
        event.registerPlacement(ModEntityTypes.SULFUR_CUBE, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, SulfurCube::checkSulfurCubeSpawnRules);
        event.registerPlacement(ModEntityTypes.PARCHED, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, MobSpawns::checkSurfaceMonstersSpawnRules);
        event.registerPlacement(ModEntityTypes.CAMEL_HUSK, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, MobSpawns::checkSurfaceMonstersSpawnRules);
        event.registerPlacement(ModEntityTypes.NAUTILUS, Type.IN_WATER, Types.MOTION_BLOCKING_NO_LEAVES, AbstractNautilus::checkNautilusSpawnRules);
        event.registerPlacement(() -> EntityType.ZOMBIE_HORSE, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, MobSpawns::checkMonsterSpawnRules, SpawnPlacementStrategy.REPLACE);
    }
    
    private static void registerAttributes(Event event) {
        event.registerAttributes(ModEntityTypes.ARMADILLO, Armadillo::createAttributes);
        event.registerAttributes(ModEntityTypes.CREAKING, Creaking::createAttributes);
        event.registerAttributes(ModEntityTypes.HAPPY_GHAST, HappyGhast::createAttributes);
        event.registerAttributes(ModEntityTypes.SULFUR_CUBE, SulfurCube::createSulfurCubeAttributes);
        event.registerAttributes(ModEntityTypes.COPPER_GOLEM, CopperGolem::createAttributes);
        event.registerAttributes(ModEntityTypes.PARCHED, Parched::createAttributes);
        event.registerAttributes(ModEntityTypes.CAMEL_HUSK, Camel::createAttributes);
        event.registerAttributes(ModEntityTypes.NAUTILUS, AbstractNautilus::createAttributes);
        event.registerAttributes(ModEntityTypes.ZOMBIE_NAUTILUS, ZombieNautilus::createAttributes);
    }
    
    private static void registerGoals(Event event) {
        event.registerGoal(EntityType.VINDICATOR, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.PILLAGER, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.ILLUSIONER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.EVOKER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        
        event.registerGoal(mob -> mob instanceof Spider, 2, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Armadillo.class, 6.0F, 1.0, 1.2, entity -> !((Armadillo) entity).isScared()));
        event.registerGoal(EntityType.IRON_GOLEM, 5, mob -> new OfferCopperGolemFlowerGoal((IronGolem) mob));
        
        event.registerGoal(mob -> mob instanceof Zombie, 2, mob -> new SpearUseGoal<>((Monster) mob, 1.0, 1.0, 10.0F, 2.0F));
        event.registerGoal(mob -> mob instanceof ZombifiedPiglin, 1, mob -> new SpearUseGoal<>((Monster) mob, 1.0, 1.0, 10.0F, 2.0F));
    }
    
    public static void bootstrap(Event event) {
        registerInteractions(event);
        registerPlacements(event);
        registerAttributes(event);
        registerGoals(event);
    }
}