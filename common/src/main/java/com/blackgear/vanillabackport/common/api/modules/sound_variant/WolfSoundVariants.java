package com.blackgear.vanillabackport.common.api.modules.sound_variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class WolfSoundVariants {
    public static final BuiltInCoreRegistry<WolfSoundVariant> REGISTRIES = BuiltInCoreRegistry.create(ResourceLocation.withDefaultNamespace("wolf_sound_variants"), VanillaBackport.NAMESPACE);
    
    public static final RegistryKey<WolfSoundVariant> CLASSIC = register(SoundSet.CLASSIC, SoundEvents.WOLF_AMBIENT, SoundEvents.WOLF_DEATH, SoundEvents.WOLF_GROWL, SoundEvents.WOLF_HURT, SoundEvents.WOLF_PANT, SoundEvents.WOLF_WHINE);
    public static final RegistryKey<WolfSoundVariant> PUGLIN = register(SoundSet.PUGLIN, ModSoundEvents.WOLF_PUGLIN_AMBIENT.get(), ModSoundEvents.WOLF_PUGLIN_DEATH.get(), ModSoundEvents.WOLF_PUGLIN_GROWL.get(), ModSoundEvents.WOLF_PUGLIN_HURT.get(), ModSoundEvents.WOLF_PUGLIN_PANT.get(), ModSoundEvents.WOLF_PUGLIN_WHINE.get());
    public static final RegistryKey<WolfSoundVariant> SAD = register(SoundSet.SAD, ModSoundEvents.WOLF_SAD_AMBIENT.get(), ModSoundEvents.WOLF_SAD_DEATH.get(), ModSoundEvents.WOLF_SAD_GROWL.get(), ModSoundEvents.WOLF_SAD_HURT.get(), ModSoundEvents.WOLF_SAD_PANT.get(), ModSoundEvents.WOLF_SAD_WHINE.get());
    public static final RegistryKey<WolfSoundVariant> ANGRY = register(SoundSet.ANGRY, ModSoundEvents.WOLF_ANGRY_AMBIENT.get(), ModSoundEvents.WOLF_ANGRY_DEATH.get(), ModSoundEvents.WOLF_ANGRY_GROWL.get(), ModSoundEvents.WOLF_ANGRY_HURT.get(), ModSoundEvents.WOLF_ANGRY_PANT.get(), ModSoundEvents.WOLF_ANGRY_WHINE.get());
    public static final RegistryKey<WolfSoundVariant> GRUMPY = register(SoundSet.GRUMPY, ModSoundEvents.WOLF_GRUMPY_AMBIENT.get(), ModSoundEvents.WOLF_GRUMPY_DEATH.get(), ModSoundEvents.WOLF_GRUMPY_GROWL.get(), ModSoundEvents.WOLF_GRUMPY_HURT.get(), ModSoundEvents.WOLF_GRUMPY_PANT.get(), ModSoundEvents.WOLF_GRUMPY_WHINE.get());
    public static final RegistryKey<WolfSoundVariant> BIG = register(SoundSet.BIG, ModSoundEvents.WOLF_BIG_AMBIENT.get(), ModSoundEvents.WOLF_BIG_DEATH.get(), ModSoundEvents.WOLF_BIG_GROWL.get(), ModSoundEvents.WOLF_BIG_HURT.get(), ModSoundEvents.WOLF_BIG_PANT.get(), ModSoundEvents.WOLF_BIG_WHINE.get());
    public static final RegistryKey<WolfSoundVariant> CUTE = register(SoundSet.CUTE, ModSoundEvents.WOLF_CUTE_AMBIENT.get(), ModSoundEvents.WOLF_CUTE_DEATH.get(), ModSoundEvents.WOLF_CUTE_GROWL.get(), ModSoundEvents.WOLF_CUTE_HURT.get(), ModSoundEvents.WOLF_CUTE_PANT.get(), ModSoundEvents.WOLF_CUTE_WHINE.get());

    private static RegistryKey<WolfSoundVariant> register(SoundSet soundSet, SoundEvent ambient, SoundEvent death, SoundEvent growl, SoundEvent hurt, SoundEvent pant, SoundEvent whine) {
        return REGISTRIES.register(soundSet.getIdentifier(), new WolfSoundVariant(ambient, death, growl, hurt, pant, whine));
    }

    public enum SoundSet {
        CLASSIC("classic"),
        PUGLIN("puglin"),
        SAD("sad"),
        ANGRY("angry"),
        GRUMPY("grumpy"),
        BIG("big"),
        CUTE("cute");

        private final String identifier;

        SoundSet(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }
}