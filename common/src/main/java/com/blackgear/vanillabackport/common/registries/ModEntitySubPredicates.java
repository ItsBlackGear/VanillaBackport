package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.predicates.PlayerPredicate;
import com.blackgear.vanillabackport.common.value_providers.TrapezoidInt;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicates;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProviderType;

import java.util.function.Supplier;

public class ModEntitySubPredicates {
    public static final CoreRegistry<MapCodec<? extends EntitySubPredicate>> REGISTRIES = CoreRegistry.create(Registries.ENTITY_SUB_PREDICATE_TYPE, VanillaBackport.MOD_ID);

    public static final Supplier<MapCodec<PlayerPredicate>> PLAYER_PREDICATE = REGISTRIES.register("player", () -> PlayerPredicate.CODEC);
}