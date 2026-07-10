package com.blackgear.vanillabackport.core.mixin.client.end_flashes;

import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashGetter;
import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashRenderer;
import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level implements EndFlashGetter {
    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract DimensionSpecialEffects effects();
    
    @Unique @Nullable private EndFlashState vb$endFlashState;
    
    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$setup(ClientPacketListener connection, ClientLevel.ClientLevelData clientLevelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionType, int viewDistance, int serverSimulationDistance, Supplier<?> profiler, LevelRenderer levelRenderer, boolean isDebug, long biomeZoomSeed, CallbackInfo ci) {
        this.vb$endFlashState = this.effects().skyType() == DimensionSpecialEffects.SkyType.END ? new EndFlashState() : null;
    }
    
    @Override
    public @Nullable EndFlashState endFlashState() {
        return this.vb$endFlashState;
    }
    
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void vb$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        EndFlashRenderer.tick(this.minecraft, (ClientLevel) (Object) this, this.vb$endFlashState);
    }
}