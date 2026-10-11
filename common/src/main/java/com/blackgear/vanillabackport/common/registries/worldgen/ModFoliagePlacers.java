package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.worldgen.tree.foliage.PoplarFoliagePlacer;
import com.blackgear.vanillabackport.common.level.worldgen.tree.trunk.PoplarTrunkPlacer;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.FoliagePlacerTypeAccessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.function.Supplier;

public class ModFoliagePlacers {
    public static final CoreRegistry<FoliagePlacerType<?>> REGISTRIES = CoreRegistry.create(Registries.FOLIAGE_PLACER_TYPE, VanillaBackport.NAMESPACE);
    
    public static final Supplier<FoliagePlacerType<PoplarFoliagePlacer>> POPLAR_FOLIAGE_PLACER = register("poplar_foliage_placer", PoplarFoliagePlacer.CODEC);
    
    private static <P extends FoliagePlacer> Supplier<FoliagePlacerType<P>> register(String name, MapCodec<P> codec) {
        return REGISTRIES.register(name, () -> FoliagePlacerTypeAccessor.createFoliagePlacerType(codec));
    }
}