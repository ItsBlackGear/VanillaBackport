package com.blackgear.vanillabackport.common.api.wolf;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class WolfSoundVariants {
    public static final ResourceKey<WolfSoundVariant> CLASSIC = register(SoundSet.CLASSIC, SoundEvents.WOLF_AMBIENT, SoundEvents.WOLF_DEATH, SoundEvents.WOLF_GROWL, SoundEvents.WOLF_HURT, SoundEvents.WOLF_PANT, SoundEvents.WOLF_WHINE);
    public static final ResourceKey<WolfSoundVariant> PUGLIN = register(SoundSet.PUGLIN, ModSoundEvents.WOLF_PUGLIN_AMBIENT.get(), ModSoundEvents.WOLF_PUGLIN_DEATH.get(), ModSoundEvents.WOLF_PUGLIN_GROWL.get(), ModSoundEvents.WOLF_PUGLIN_HURT.get(), ModSoundEvents.WOLF_PUGLIN_PANT.get(), ModSoundEvents.WOLF_PUGLIN_WHINE.get());
    public static final ResourceKey<WolfSoundVariant> SAD = register(SoundSet.SAD, ModSoundEvents.WOLF_SAD_AMBIENT.get(), ModSoundEvents.WOLF_SAD_DEATH.get(), ModSoundEvents.WOLF_SAD_GROWL.get(), ModSoundEvents.WOLF_SAD_HURT.get(), ModSoundEvents.WOLF_SAD_PANT.get(), ModSoundEvents.WOLF_SAD_WHINE.get());
    public static final ResourceKey<WolfSoundVariant> ANGRY = register(SoundSet.ANGRY, ModSoundEvents.WOLF_ANGRY_AMBIENT.get(), ModSoundEvents.WOLF_ANGRY_DEATH.get(), ModSoundEvents.WOLF_ANGRY_GROWL.get(), ModSoundEvents.WOLF_ANGRY_HURT.get(), ModSoundEvents.WOLF_ANGRY_PANT.get(), ModSoundEvents.WOLF_ANGRY_WHINE.get());
    public static final ResourceKey<WolfSoundVariant> GRUMPY = register(SoundSet.GRUMPY, ModSoundEvents.WOLF_GRUMPY_AMBIENT.get(), ModSoundEvents.WOLF_GRUMPY_DEATH.get(), ModSoundEvents.WOLF_GRUMPY_GROWL.get(), ModSoundEvents.WOLF_GRUMPY_HURT.get(), ModSoundEvents.WOLF_GRUMPY_PANT.get(), ModSoundEvents.WOLF_GRUMPY_WHINE.get());
    public static final ResourceKey<WolfSoundVariant> BIG = register(SoundSet.BIG, ModSoundEvents.WOLF_BIG_AMBIENT.get(), ModSoundEvents.WOLF_BIG_DEATH.get(), ModSoundEvents.WOLF_BIG_GROWL.get(), ModSoundEvents.WOLF_BIG_HURT.get(), ModSoundEvents.WOLF_BIG_PANT.get(), ModSoundEvents.WOLF_BIG_WHINE.get());
    public static final ResourceKey<WolfSoundVariant> CUTE = register(SoundSet.CUTE, ModSoundEvents.WOLF_CUTE_AMBIENT.get(), ModSoundEvents.WOLF_CUTE_DEATH.get(), ModSoundEvents.WOLF_CUTE_GROWL.get(), ModSoundEvents.WOLF_CUTE_HURT.get(), ModSoundEvents.WOLF_CUTE_PANT.get(), ModSoundEvents.WOLF_CUTE_WHINE.get());

    private static ResourceKey<WolfSoundVariant> register(SoundSet soundSet, SoundEvent ambient, SoundEvent death, SoundEvent growl, SoundEvent hurt, SoundEvent pant, SoundEvent whine) {
        return ModBuiltinRegistries.VARIANTS.resource(soundSet.getIdentifier(), new WolfSoundVariant(ambient, death, growl, hurt, pant, whine));
    }

    public enum SoundSet {
        CLASSIC("classic", ""),
        PUGLIN("puglin", "_puglin"),
        SAD("sad", "_sad"),
        ANGRY("angry", "_angry"),
        GRUMPY("grumpy", "_grumpy"),
        BIG("big", "_big"),
        CUTE("cute", "_cute");

        private final String identifier;
        private final String soundEventSuffix;

        SoundSet(String identifier, String soundEventSuffix) {
            this.identifier = identifier;
            this.soundEventSuffix = soundEventSuffix;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getSoundEventSuffix() {
            return this.soundEventSuffix;
        }
    }
}