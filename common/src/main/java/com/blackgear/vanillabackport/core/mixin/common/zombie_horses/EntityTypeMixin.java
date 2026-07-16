package com.blackgear.vanillabackport.core.mixin.common.zombie_horses;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityType.class)
public class EntityTypeMixin<T extends Entity> {
    @Mutable @Shadow @Final private MobCategory category;
    
    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$replaceEntityValues(
        EntityType.EntityFactory<T> factory,
        MobCategory category,
        boolean serialize,
        boolean summon,
        boolean fireImmune,
        boolean canSpawnFarFromPlayer,
        ImmutableSet<Block> immuneTo,
        EntityDimensions dimensions,
        float spawnDimensionsScale,
        int clientTrackingRange,
        int updateInterval,
        FeatureFlagSet requiredFeatures,
        CallbackInfo ci
    ) {
        EntityType<T> self = (EntityType<T>)(Object) this;
        if (self == EntityType.ZOMBIE_HORSE) {
            this.category = MobCategory.MONSTER;
        }
    }
}