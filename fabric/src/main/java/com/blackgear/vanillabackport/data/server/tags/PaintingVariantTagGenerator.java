package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModPaintingVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;

import java.util.concurrent.CompletableFuture;

public class PaintingVariantTagGenerator extends TagsProvider<PaintingVariant> {
    public PaintingVariantTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.PAINTING_VARIANT, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PaintingVariantTags.PLACEABLE).add(ModPaintingVariants.DENNIS);
    }
}