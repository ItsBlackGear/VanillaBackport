package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.common.block.WoodTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType PALE_OAK = register("pale_oak", ModBlockSetTypes.PALE_OAK);

    private static WoodType register(String name, BlockSetType set) {
        return WoodTypeRegistry.create(ResourceLocation.withDefaultNamespace(name), set);
    }
}