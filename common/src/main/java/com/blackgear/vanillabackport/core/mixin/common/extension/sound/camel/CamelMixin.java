package com.blackgear.vanillabackport.core.mixin.common.extension.sound.camel;

import com.blackgear.vanillabackport.common.api.extensions.entity.modifiers.CamelSoundModifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.camel.Camel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camel.class)
public class CamelMixin {
    @ModifyArg(
        method = "handleStartJump",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/camel/Camel;makeSound(Lnet/minecraft/sounds/SoundEvent;)V",
            ordinal = 0))
    private SoundEvent vb$getDashingSound(SoundEvent original) {
        CamelSoundModifier sound = CamelSoundModifier.of((Camel)(Object)this);
        return sound != null ? sound.getDashingSound() : original;
    }
    
    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
            ordinal = 0),
        index = 2)
    private SoundEvent vb$getDashReadySound(SoundEvent original) {
        CamelSoundModifier sound = CamelSoundModifier.of((Camel)(Object)this);
        return sound != null ? sound.getDashReadySound() : original;
    }
    
    @ModifyArg(
        method = "standUp",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/camel/Camel;makeSound(Lnet/minecraft/sounds/SoundEvent;)V",
            ordinal = 0))
    private SoundEvent vb$getStandUpSound(SoundEvent original) {
        CamelSoundModifier sound = CamelSoundModifier.of((Camel)(Object)this);
        return sound != null ? sound.getStandUpSound() : original;
    }
    
    @ModifyArg(
        method = "sitDown",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/camel/Camel;makeSound(Lnet/minecraft/sounds/SoundEvent;)V",
            ordinal = 0))
    private SoundEvent vb$getSitDownSound(SoundEvent original) {
        CamelSoundModifier sound = CamelSoundModifier.of((Camel)(Object)this);
        return sound != null ? sound.getSitDownSound() : original;
    }
}