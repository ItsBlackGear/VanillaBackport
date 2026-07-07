package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.vanillabackport.common.integrations.interactions.GhastHarnessInteraction;
import com.blackgear.vanillabackport.common.integrations.interactions.LeashIntegration;
import com.blackgear.vanillabackport.common.integrations.interactions.ShearEquipmentInteraction;
import com.blackgear.vanillabackport.common.integrations.interactions.WolfArmorInteraction;
import com.blackgear.vanillabackport.common.level.entity.ai.goal.OfferCopperGolemFlowerGoal;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Spider;

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
        event.registerPlacement(() -> EntityType.CAMEL, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, (type, level, reason, pos, random) -> level.getBlockState(pos.below()).is(ModBlockTags.CAMELS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8);
        event.registerPlacement(ModEntityTypes.SULFUR_CUBE, Type.ON_GROUND, Types.MOTION_BLOCKING_NO_LEAVES, SulfurCube::checkSulfurCubeSpawnRules);
    }
    
    private static void registerAttributes(Event event) {
        event.registerAttributes(ModEntityTypes.ARMADILLO, Armadillo::createAttributes);
        event.registerAttributes(ModEntityTypes.CREAKING, Creaking::createAttributes);
        event.registerAttributes(ModEntityTypes.HAPPY_GHAST, HappyGhast::createAttributes);
        event.registerAttributes(ModEntityTypes.SULFUR_CUBE, SulfurCube::createSulfurCubeAttributes);
        event.registerAttributes(ModEntityTypes.COPPER_GOLEM, CopperGolem::createAttributes);
    }
    
    private static void registerGoals(Event event) {
        event.registerGoal(EntityType.VINDICATOR, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.PILLAGER, 1, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.ILLUSIONER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        event.registerGoal(EntityType.EVOKER, 3, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Creaking.class, 8.0F, 0.6, 1.2));
        
        event.registerGoal(mob -> mob instanceof Spider, 2, mob -> new AvoidEntityGoal<>((PathfinderMob) mob, Armadillo.class, 6.0F, 1.0, 1.2, entity -> !((Armadillo) entity).isScared()));
        event.registerGoal(EntityType.IRON_GOLEM, 5, mob -> new OfferCopperGolemFlowerGoal((IronGolem) mob));
    }
    
    public static void bootstrap(Event event) {
        registerInteractions(event);
        registerPlacements(event);
        registerAttributes(event);
        registerGoals(event);
    }
}