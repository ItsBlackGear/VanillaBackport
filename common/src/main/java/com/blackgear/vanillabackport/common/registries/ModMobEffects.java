package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.effect.AdvanceMobEffect;
import com.blackgear.vanillabackport.common.level.effect.RaidOmenMobEffect;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

// ModMobEffects class. - Echo2craft.
public class ModMobEffects {
    public static final CoreRegistry<MobEffect> MOB_EFFECTS = CoreRegistry.create(Registries.MOB_EFFECT, VanillaBackport.NAMESPACE);

    public static final Supplier<MobEffect> RAID_OMEN = MOB_EFFECTS.register(
            "raid_omen",
            () -> new RaidOmenMobEffect(
                    MobEffectCategory.NEUTRAL,
                    14565464,
                    ModParticles.RAID_OMEN::get
            ).withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_RAID_OMEN.get())
    );
    public static final Supplier<MobEffect> TRIAL_OMEN = MOB_EFFECTS.register(
            "trial_omen",
            () -> new AdvanceMobEffect(
                    MobEffectCategory.NEUTRAL,
                    1484454,
                    ModParticles.TRIAL_OMEN::get
            ).withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_TRIAL_OMEN.get())
    );

}
