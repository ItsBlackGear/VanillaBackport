package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.boat.PaleOakChestBoat;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ModEntities {
    public static final EntityRegistry ENTITIES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<EntityType<Creaking>> CREAKING = ENTITIES.entity(
        "creaking",
        EntityType.Builder.of(Creaking::new, MobCategory.MONSTER)
            .sized(0.9F, 2.7F)
            .eyeHeight(2.3F)
            .clientTrackingRange(8)
    );
    public static final Supplier<EntityType<HappyGhast>> HAPPY_GHAST = ENTITIES.entity(
        "happy_ghast",
        EntityType.Builder.of(HappyGhast::new, MobCategory.CREATURE)
            .sized(4.0F, 4.0F)
            .eyeHeight(2.6F)
            .passengerAttachments(new Vec3(0.0, 4.0, 1.8), new Vec3(-1.8, 4.0, 0.0), new Vec3(0.0, 4.0, -1.8), new Vec3(1.8, 4.0, 0.0))
            .ridingOffset(0.5F)
            .clientTrackingRange(10)
    );

    public static final Supplier<EntityType<PaleOakBoat>> PALE_OAK_BOAT = ENTITIES.entity(
        "pale_oak_boat",
        EntityType.Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
    );
    public static final Supplier<EntityType<PaleOakChestBoat>> PALE_OAK_CHEST_BOAT = ENTITIES.entity(
        "pale_oak_chest_boat",
        EntityType.Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .clientTrackingRange(10)
    );
}