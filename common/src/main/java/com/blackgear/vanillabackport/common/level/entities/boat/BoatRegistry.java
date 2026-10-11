package com.blackgear.vanillabackport.common.level.entities.boat;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public final class BoatRegistry {
    public static final ResourceLocation DEFAULT_TYPE = ResourceLocation.withDefaultNamespace("oak");
    private static final Map<ResourceLocation, BoatType> TYPES = new ConcurrentHashMap<>();
    
    static {
        register(DEFAULT_TYPE, () -> Items.OAK_BOAT, () -> Items.OAK_CHEST_BOAT, () -> Blocks.OAK_PLANKS, false);
    }
    
    public static BoatType register(ResourceLocation id, Supplier<Item> boat, Supplier<Item> chestBoat, Supplier<Block> planks, boolean isRaft) {
        BoatType type = new BoatType(id, boat, chestBoat, planks, isRaft);
        TYPES.put(id, type);
        return type;
    }
    
    public static BoatType getType(ResourceLocation id) {
        return TYPES.getOrDefault(id, TYPES.get(DEFAULT_TYPE));
    }
    
    public static Collection<BoatType> getAllTypes() {
        return TYPES.values();
    }
    
    @Environment(EnvType.CLIENT)
    public static Map<BoatType, Pair<ResourceLocation, ListModel<Boat>>> createModels(EntityRendererProvider.Context context, boolean chest) {
        Map<BoatType, Pair<ResourceLocation, ListModel<Boat>>> map = new IdentityHashMap<>();
        for (BoatType type : TYPES.values()) {
            ResourceLocation texture = chest ? type.chestTexture() : type.texture();
            ListModel<Boat> model = createModel(context, type, chest);
            map.put(type, Pair.of(texture, model));
        }
        
        return map;
    }
    
    @Environment(EnvType.CLIENT)
    private static ListModel<Boat> createModel(EntityRendererProvider.Context context, BoatType type, boolean chest) {
        ModelLayerLocation layer = chest ? ModModelLayers.CUSTOM_CHEST_BOAT : ModModelLayers.CUSTOM_BOAT;
        ModelPart part = context.bakeLayer(layer);
        
        if (type.isRaft()) {
            return chest ? new ChestRaftModel(part) : new RaftModel(part);
        }
        
        return chest ? new ChestBoatModel(part) : new BoatModel(part);
    }
    
    public record BoatType(
        ResourceLocation id,
        Supplier<Item> boatItem,
        Supplier<Item> chestBoatItem,
        Supplier<Block> planksBlock,
        boolean isRaft,
        ResourceLocation texture,
        ResourceLocation chestTexture
    ) {
        public BoatType(ResourceLocation id, Supplier<Item> boatItem, Supplier<Item> chestBoatItem, Supplier<Block> planksBlock, boolean isRaft) {
            this(
                id,
                boatItem,
                chestBoatItem,
                planksBlock,
                isRaft,
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/entity/boat/" + id.getPath() + ".png"),
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/entity/chest_boat/" + id.getPath() + ".png")
            );
        }
        
        public Item getBoat() { return this.boatItem.get(); }
        public Item getChestBoat() { return this.chestBoatItem.get(); }
        public Block getPlanks() { return this.planksBlock.get(); }
    }
}