package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.client.resources.sounds.SoundInstance;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.sounds.MusicManager.class)
public interface MusicManagerAccessor {
    @Accessor
    SoundInstance getCurrentMusic();
}
