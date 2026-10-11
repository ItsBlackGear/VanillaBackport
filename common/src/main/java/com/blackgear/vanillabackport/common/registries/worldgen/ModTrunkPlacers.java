package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.worldgen.tree.trunk.PoplarTrunkPlacer;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.function.Supplier;

public class ModTrunkPlacers {
    public static final CoreRegistry<TrunkPlacerType<?>> REGISTRIES = CoreRegistry.create(Registries.TRUNK_PLACER_TYPE, VanillaBackport.NAMESPACE);
    
    public static final Supplier<TrunkPlacerType<PoplarTrunkPlacer>> POPLAR_TRUNK_PLACER = register("poplar_trunk_placer", PoplarTrunkPlacer.CODEC);
    
    private static <P extends TrunkPlacer> Supplier<TrunkPlacerType<P>> register(String name, Codec<P> codec) {
        return REGISTRIES.register(name, () -> new TrunkPlacerType<>(codec));
    }
}