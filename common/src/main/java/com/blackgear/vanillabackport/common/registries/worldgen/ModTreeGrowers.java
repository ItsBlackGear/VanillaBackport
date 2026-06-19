package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower PALE_OAK_TREE = new TreeGrower("pale_oak",
        Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL),
        Optional.empty(),
        Optional.empty());
}