package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.ModTrimMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.concurrent.CompletableFuture;

public class TrimMaterialGenerator extends FabricDynamicRegistryProvider {
    public TrimMaterialGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModTrimMaterials.RESIN);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<TrimMaterial> key) {
        final HolderLookup.RegistryLookup<TrimMaterial> registry = provider.lookupOrThrow(Registries.TRIM_MATERIAL);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "trim_materials";
    }
}