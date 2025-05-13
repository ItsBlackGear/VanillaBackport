package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Parrot.class)
public class ParrotMixin {
    @Shadow @Final static Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP;

    @Inject(
        method = "<clinit>",
        at = @At("RETURN")
    )
    private static void addCustomMobSounds(CallbackInfo ci) {
        MOB_SOUND_MAP.put(ModEntities.CREAKING.get(), ModSoundEvents.PARROT_IMITATE_CREAKING.get());
    }
}