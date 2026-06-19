package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.entities.ModDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class DamageTypeGenerator extends FabricDynamicRegistryProvider {
    public DamageTypeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModDamageTypes.SULFUR_CUBE_HOT);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<DamageType> key) {
        final HolderLookup.RegistryLookup<DamageType> registry = provider.lookupOrThrow(Registries.DAMAGE_TYPE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "damage_types";
    }
}