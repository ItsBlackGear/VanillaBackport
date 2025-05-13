package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor
    Map<SoundInstance, ChannelAccess.ChannelHandle> getInstanceToChannel();

    @Accessor
    boolean isLoaded();

    @Invoker
    float callCalculateVolume(SoundInstance instance);
}