package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakChestBoat;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.core.registries.neo_registries.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.neo_registries.VanillaRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static final VanillaRegistry<EntityType<?>> REGISTRIES = VanillaRegistry.create(Registries.ENTITY_TYPE);

    // Armored Paws
    
    public static final FeatureHolder<EntityType<Armadillo>> ARMADILLO = register("armadillo",
        EntityType.Builder.of(Armadillo::new, MobCategory.CREATURE)
            .sized(0.7F, 0.65F)
            .clientTrackingRange(10));
    
    // The Garden Awakens
    
    public static final FeatureHolder<EntityType<Creaking>> CREAKING = register("creaking",
        EntityType.Builder.of(Creaking::new, MobCategory.MONSTER)
            .sized(0.9F, 2.7F)
            .clientTrackingRange(8));
    
    public static final FeatureHolder<EntityType<PaleOakBoat>> PALE_OAK_BOAT = register("pale_oak_boat",
        EntityType.Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10));
    public static final FeatureHolder<EntityType<PaleOakChestBoat>> PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat",
        EntityType.Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10));
    
    // Chase the Skies
    
    public static final FeatureHolder<EntityType<HappyGhast>> HAPPY_GHAST = register("happy_ghast",
        EntityType.Builder.of(HappyGhast::new, MobCategory.CREATURE)
            .sized(4.0F, 4.0F)
            .clientTrackingRange(10));
    
    // Chaos Cubed
    
    public static final FeatureHolder<EntityType<SulfurCube>> SULFUR_CUBE = register("sulfur_cube",
        EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .clientTrackingRange(10));
    
    // Helper Methods
    
    public static <T extends Entity> FeatureHolder<EntityType<T>> register(String name, EntityType.Builder<T> entity) {
        return REGISTRIES.register(name, () -> entity.build(name));
    }
}