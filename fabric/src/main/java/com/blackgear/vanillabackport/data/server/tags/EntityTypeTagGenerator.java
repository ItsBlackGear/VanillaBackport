package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagGenerator extends FabricTagProvider.EntityTypeTagProvider {
    public EntityTypeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(EntityTypeTags.ZOMBIES)
            .add(ModEntityTypes.CAMEL_HUSK.get());
        
        this.getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
            .add(ModEntityTypes.HAPPY_GHAST.get());

        this.getOrCreateTagBuilder(EntityTypeTags.DISMOUNTS_UNDERWATER)
            .add(ModEntityTypes.HAPPY_GHAST.get());

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
                ModEntityTypes.HAPPY_GHAST.get(),
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

        this.getOrCreateTagBuilder(ModEntityTypeTags.MONSTERS_THAT_SPAWN_ON_PEACEFUL)
            .add(
                EntityType.PIGLIN,
                EntityType.HOGLIN,
                ModEntityTypes.SULFUR_CUBE.get()
            );

        this.getOrCreateTagBuilder(ModEntityTypeTags.NOT_AFFECTED_BY_GEYSERS)
            .add(
                EntityType.ENDER_DRAGON
            );
        
        this.getOrCreateTagBuilder(ModEntityTypeTags.ACCEPTS_IRON_GOLEM_GIFT)
            .add(ModEntityTypes.COPPER_GOLEM.get());
        
        this.getOrCreateTagBuilder(ModEntityTypeTags.CAN_FLOAT_WHILE_RIDDEN)
            .add(EntityType.HORSE, EntityType.ZOMBIE_HORSE, EntityType.MULE, EntityType.DONKEY, EntityType.CAMEL, ModEntityTypes.CAMEL_HUSK.get());
    }
}