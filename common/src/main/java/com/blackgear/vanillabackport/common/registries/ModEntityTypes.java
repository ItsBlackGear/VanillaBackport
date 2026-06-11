package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.common.level.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.boat.PaleOakChestBoat;
import com.blackgear.vanillabackport.common.level.entities.armadillo.Armadillo;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entities.sulfurcube.SulfurCube;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class ModEntityTypes {
    public static final LinkedHashMap<ResourceLocation, EntityType<?>> REGISTRIES = new LinkedHashMap<>();

    // Armored Paws
    
    public static final EntityType<Armadillo> ARMADILLO = register("armadillo",
        EntityType.Builder.of(Armadillo::new, MobCategory.CREATURE)
            .sized(0.7F, 0.65F)
            .clientTrackingRange(10)
    );
    
    // The Garden Awakens
    
    public static final EntityType<Creaking> CREAKING = register("creaking",
        EntityType.Builder.of(Creaking::new, MobCategory.MONSTER)
            .sized(0.9F, 2.7F)
            .clientTrackingRange(8)
    );
    public static final EntityType<PaleOakBoat> PALE_OAK_BOAT = register("pale_oak_boat",
        EntityType.Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
    );
    public static final EntityType<PaleOakChestBoat> PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat",
        EntityType.Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
    );
    
    // Chase the Skies
    
    public static final EntityType<HappyGhast> HAPPY_GHAST = register("happy_ghast",
        EntityType.Builder.of(HappyGhast::new, MobCategory.CREATURE)
            .sized(4.0F, 4.0F)
            .clientTrackingRange(10)
    );
    
    // Chaos Cubed
    
    public static final EntityType<SulfurCube> SULFUR_CUBE = register("sulfur_cube",
        EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .clientTrackingRange(10)
    );

    // Helper Methods
    
    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> factory) {
        EntityType<T> entry = factory.build(name);
        REGISTRIES.put(new ResourceLocation(name), entry);
        return entry;
    }

    public static void bootstrap(BiConsumer<ResourceLocation, EntityType<?>> consumer) {
        REGISTRIES.forEach(consumer);
    }
}
