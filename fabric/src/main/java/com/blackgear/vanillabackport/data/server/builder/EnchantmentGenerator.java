package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends FabricDynamicRegistryProvider {
    public EnchantmentGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModEnchantments.LUNGE);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<Enchantment> key) {
        final HolderLookup.RegistryLookup<Enchantment> registry = provider.lookupOrThrow(Registries.ENCHANTMENT);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "enchantments";
    }
}