package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagGenerator extends FabricTagProvider.EnchantmentTagProvider {
    public EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .add(ModEnchantments.LUNGE);
    }
}