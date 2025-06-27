package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType PALE_OAK = register("pale_oak", ModBlockSetTypes.PALE_OAK);

    private static WoodType register(String name, BlockSetType set) {
        return WoodTypeRegistry.create(VanillaBackport.vanilla(name), set);
    }
}