package com.blackgear.vanillabackport.core.registries.neo_registries.handlers;

import com.blackgear.vanillabackport.core.registries.neo_registries.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.neo_registries.VanillaRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class VanillaItemRegistry {
    private final VanillaRegistry<Item> items;
    
    private VanillaItemRegistry() {
        this.items = VanillaRegistry.create(Registries.ITEM);
    }
    
    public static VanillaItemRegistry create() {
        return new VanillaItemRegistry();
    }
    
    public <T extends Item> FeatureHolder<T> register(String name, Supplier<T> item) {
        return this.items.register(name, item);
    }
    
    public FeatureHolder<Item> register(String name) {
        return this.register(name, new Item.Properties());
    }
    
    public FeatureHolder<Item> register(String name, Item.Properties properties) {
        return this.register(name, Item::new, properties);
    }
    
    public FeatureHolder<Item> register(String name, Function<Item.Properties, Item> function) {
        return this.register(name, function, new Item.Properties());
    }
    
    public FeatureHolder<Item> register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return this.items.register(name, () -> factory.apply(properties));
    }
    
    public void register() {
        this.items.register();
    }
}