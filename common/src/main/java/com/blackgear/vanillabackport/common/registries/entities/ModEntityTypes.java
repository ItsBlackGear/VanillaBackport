package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.entity.boat.PaleOakChestBoat;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.sulfur_cube.SulfurCube;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final EntityRegistry REGISTRIES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    // The Garden Awakens
    
    public static final Supplier<EntityType<Creaking>> CREAKING = REGISTRIES.entity("creaking",
        EntityType.Builder.of(Creaking::new, MobCategory.MONSTER)
            .sized(0.9F, 2.7F)
            .eyeHeight(2.3F)
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
            .eyeHeight(2.6F)
            .passengerAttachments(new Vec3(0.0, 4.0, 1.8), new Vec3(-1.8, 4.0, 0.0), new Vec3(0.0, 4.0, -1.8), new Vec3(1.8, 4.0, 0.0))
            .ridingOffset(0.5F)
            .clientTrackingRange(10));
    
    // Chaos Cubed
    
    public static final Supplier<EntityType<SulfurCube>> SULFUR_CUBE = REGISTRIES.entity("sulfur_cube",
        EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .eyeHeight(0.175F)
            .spawnDimensionsScale(2.0F)
            .clientTrackingRange(10));
}