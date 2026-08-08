package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.triggers.SpearMobsTrigger;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class ModCriteriaTriggers {
    public static final CoreRegistry<CriterionTrigger<?>> REGISTRIES = CoreRegistry.create(Registries.TRIGGER_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<SpearMobsTrigger> SPEAR_MOBS_TRIGGER = REGISTRIES.register("spear_mobs", SpearMobsTrigger::new);
}