package com.blackgear.vanillabackport.core.mixin.common.weathering_lightning_rods;

import com.blackgear.vanillabackport.common.registries.blocks.ModPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    @Shadow public abstract PoiManager getPoiManager();
    
    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }
    
    @Inject(method = "findLightningRod", at = @At("HEAD"), cancellable = true)
    private void findLightningRods(BlockPos center, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        Optional<BlockPos> nearbyLightningRod = this.getPoiManager()
            .findClosest(
                p -> p.is(ModPoiTypes.LIGHTNING_RODS),
                pos -> pos.getY() == this.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1,
                center,
                128,
                PoiManager.Occupancy.ANY
            );
        cir.setReturnValue(nearbyLightningRod.map(pos -> pos.above(1)));
    }
}