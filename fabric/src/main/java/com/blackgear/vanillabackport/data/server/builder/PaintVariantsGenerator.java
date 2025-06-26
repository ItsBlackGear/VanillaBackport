package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.ModPaintingVariants;
import com.blackgear.vanillabackport.common.registries.ModTrimMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.concurrent.CompletableFuture;

public class PaintVariantsGenerator extends FabricDynamicRegistryProvider {
    public PaintVariantsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModPaintingVariants.DENNIS);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<PaintingVariant> key) {
        final HolderLookup.RegistryLookup<PaintingVariant> registry = provider.lookupOrThrow(Registries.PAINTING_VARIANT);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "painting_variants";
    }
}