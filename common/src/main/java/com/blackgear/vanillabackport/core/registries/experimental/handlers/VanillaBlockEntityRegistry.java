package com.blackgear.vanillabackport.core.registries.experimental.handlers;

import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.blackgear.vanillabackport.core.registries.experimental.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.experimental.VanillaRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class VanillaBlockEntityRegistry {
    private final VanillaRegistry<BlockEntityType<?>> registry;
    
    private VanillaBlockEntityRegistry() {
        this.registry = VanillaRegistry.create(Registries.BLOCK_ENTITY_TYPE);
    }
    
    public static VanillaBlockEntityRegistry create() {
        return new VanillaBlockEntityRegistry();
    }
    
    public <T extends BlockEntity> FeatureHolder<BlockEntityType<T>> register(String name, BlockEntityTypeBuilder<T> builder) {
        return this.registry.register(name, builder::build);
    }
    
    public void register() {
        this.registry.register();
    }
}