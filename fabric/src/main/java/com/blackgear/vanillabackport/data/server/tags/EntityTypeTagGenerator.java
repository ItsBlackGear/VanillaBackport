package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends FabricTagProvider.EntityTypeTagProvider {
    public EntityTypeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(ModEntityTypeTags.FOLLOWABLE_FRIENDLY_MOBS)
            .add(
                EntityType.BEE,
                EntityType.CAMEL,
                EntityType.CAT,
                EntityType.CHICKEN,
                EntityType.COW,
                EntityType.DONKEY,
                EntityType.FOX,
                EntityType.GOAT,
                EntityType.HORSE,
                EntityType.SKELETON_HORSE,
                EntityType.LLAMA,
                EntityType.MULE,
                EntityType.OCELOT,
                EntityType.PANDA,
                EntityType.PARROT,
                EntityType.PIG,
                EntityType.POLAR_BEAR,
                EntityType.RABBIT,
                EntityType.SHEEP,
                EntityType.SNIFFER,
                EntityType.STRIDER,
                EntityType.VILLAGER,
                EntityType.WOLF
            );

    }
}