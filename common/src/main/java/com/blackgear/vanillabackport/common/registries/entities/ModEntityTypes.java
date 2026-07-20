package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakChestBoat;
import com.blackgear.vanillabackport.common.level.entity.decoration.Cushion;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.camel.CamelHusk;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.Nautilus;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilus;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.skeleton.Parched;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final EntityRegistry REGISTRIES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    // Armored Paws
    
    public static final Supplier<EntityType<Armadillo>> ARMADILLO = REGISTRIES.entity("armadillo",
        EntityType.Builder.of(Armadillo::new, MobCategory.CREATURE)
            .sized(0.7F, 0.65F)
            .clientTrackingRange(10));
    
    // The Garden Awakens
    
    public static final Supplier<EntityType<Creaking>> CREAKING = REGISTRIES.entity("creaking",
        EntityType.Builder.of(Creaking::new, MobCategory.MONSTER)
            .sized(0.9F, 2.7F)
            .clientTrackingRange(8));
    
    public static final Supplier<EntityType<PaleOakBoat>> PALE_OAK_BOAT = REGISTRIES.entity("pale_oak_boat",
        EntityType.Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10));
    public static final Supplier<EntityType<PaleOakChestBoat>> PALE_OAK_CHEST_BOAT = REGISTRIES.entity("pale_oak_chest_boat",
        EntityType.Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10));
    
    // Chase the Skies
    
    public static final Supplier<EntityType<HappyGhast>> HAPPY_GHAST = REGISTRIES.entity("happy_ghast",
        EntityType.Builder.of(HappyGhast::new, MobCategory.CREATURE)
            .sized(4.0F, 4.0F)
            .clientTrackingRange(10));
    
    // Copper Age
    
    public static final Supplier<EntityType<CopperGolem>> COPPER_GOLEM = REGISTRIES.entity("copper_golem",
        EntityType.Builder.of(CopperGolem::new, MobCategory.MISC)
            .sized(0.49F, 0.98F)
            .clientTrackingRange(10));
    
    // Mounts of Mayhem
    
    public static final Supplier<EntityType<Parched>> PARCHED = REGISTRIES.entity("parched",
        EntityType.Builder.of(Parched::new, MobCategory.MONSTER)
            .sized(0.6F, 1.99F)
            .clientTrackingRange(8));
    public static final Supplier<EntityType<CamelHusk>> CAMEL_HUSK = REGISTRIES.entity("camel_husk",
        EntityType.Builder.of(CamelHusk::new, MobCategory.MONSTER)
            .sized(1.7F, 2.375F)
            .clientTrackingRange(10));
    public static final Supplier<EntityType<Nautilus>> NAUTILUS = REGISTRIES.entity("nautilus",
        EntityType.Builder.of(Nautilus::new, MobCategory.WATER_CREATURE)
            .sized(0.875F, 0.95F)
            .clientTrackingRange(10));
    public static final Supplier<EntityType<ZombieNautilus>> ZOMBIE_NAUTILUS = REGISTRIES.entity("zombie_nautilus",
        EntityType.Builder.of(ZombieNautilus::new, MobCategory.MONSTER)
            .sized(0.875F, 0.95F)
            .clientTrackingRange(10));
    
    // Chaos Cubed
    
    public static final Supplier<EntityType<SulfurCube>> SULFUR_CUBE = REGISTRIES.entity("sulfur_cube",
        EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .clientTrackingRange(10));
    
    // Miscellaneous
    
    public static final Supplier<EntityType<Cushion>> CUSHION = REGISTRIES.entity("cushion",
        EntityType.Builder.of(Cushion::new, MobCategory.MISC)
            .sized(1.0F, 0.25F)
            .clientTrackingRange(10)
            .updateInterval(Integer.MAX_VALUE));
}