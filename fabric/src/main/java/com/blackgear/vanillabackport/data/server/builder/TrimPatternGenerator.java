package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.ModTrimMaterials;
import com.blackgear.vanillabackport.common.registries.ModTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TrimPatternGenerator extends FabricDynamicRegistryProvider {
    public TrimPatternGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        this.add(registries, entries, ModTrimPatterns.FLOW);
        this.add(registries, entries, ModTrimPatterns.BOLT);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<TrimPattern> key) {
        final HolderLookup.RegistryLookup<TrimPattern> registry = provider.lookupOrThrow(Registries.TRIM_PATTERN);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public @NotNull String getName() {
        return "trim_patterns";
    }
}
