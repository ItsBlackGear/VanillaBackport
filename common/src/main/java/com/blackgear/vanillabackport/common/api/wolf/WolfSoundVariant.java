package com.blackgear.vanillabackport.common.api.wolf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public record WolfSoundVariant(
    Holder<SoundEvent> ambientSound,
    Holder<SoundEvent> deathSound,
    Holder<SoundEvent> growlSound,
    Holder<SoundEvent> hurtSound,
    Holder<SoundEvent> pantSound,
    Holder<SoundEvent> whineSound
) {
    public WolfSoundVariant(SoundEvent ambientSound, SoundEvent deathSound, SoundEvent growlSound, SoundEvent hurtSound, SoundEvent pantSound, SoundEvent whineSound) {
        this(Holder.direct(ambientSound), Holder.direct(deathSound), Holder.direct(growlSound), Holder.direct(hurtSound), Holder.direct(pantSound), Holder.direct(whineSound));
    }

    public static final Codec<WolfSoundVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("ambient_sound").forGetter(WolfSoundVariant::ambientSound),
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("death_sound").forGetter(WolfSoundVariant::deathSound),
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("growl_sound").forGetter(WolfSoundVariant::growlSound),
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("hurt_sound").forGetter(WolfSoundVariant::hurtSound),
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("pant_sound").forGetter(WolfSoundVariant::pantSound),
        BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("whine_sound").forGetter(WolfSoundVariant::whineSound)
    ).apply(instance, WolfSoundVariant::new));
}