package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.ModJukeboxSongs;
import com.blackgear.vanillabackport.common.registries.ModTrimMaterials;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.concurrent.CompletableFuture;

public class JukeboxSongGenerator extends FabricDynamicRegistryProvider {
    public JukeboxSongGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModJukeboxSongs.TEARS);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<JukeboxSong> key) {
        final HolderLookup.RegistryLookup<JukeboxSong> registry = provider.lookupOrThrow(Registries.JUKEBOX_SONG);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "jukebox_songs";
    }
}