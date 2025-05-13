package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends TagsProvider<Biome> {
    public BiomeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.BIOME, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BiomeTags.IS_FOREST).add(ModBiomes.PALE_GARDEN);
        this.tag(BiomeTags.HAS_WOODLAND_MANSION).add(ModBiomes.PALE_GARDEN);
        this.tag(BiomeTags.STRONGHOLD_BIASED_TO).add(ModBiomes.PALE_GARDEN);
        this.tag(BiomeTags.IS_OVERWORLD).add(ModBiomes.PALE_GARDEN);
    }
}