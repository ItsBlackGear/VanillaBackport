package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Environment(EnvType.CLIENT)
public class GhastEquipmentManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final GhastEquipmentManager INSTANCE = new GhastEquipmentManager();
    
    private static final Map<ItemLike, ResourceLocation> DATADRIVEN_EQUIPMENT = new ConcurrentHashMap<>();
    private static final Map<ItemLike, ResourceLocation> HARDCODED_EQUIPMENT = new ConcurrentHashMap<>();
    
    public GhastEquipmentManager() {
        super(GSON, "equipment/ghast");
    }
    
    public static void register(ItemLike stack, ResourceLocation texture) {
        HARDCODED_EQUIPMENT.put(stack, texture);
    }
    
    @Nullable
    public static ResourceLocation getTexture(ItemLike item) {
        ResourceLocation texture = DATADRIVEN_EQUIPMENT.get(item);
        return texture != null ? texture : HARDCODED_EQUIPMENT.get(item);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        Map<ItemLike, ResourceLocation> entries = new HashMap<>();
        
        map.forEach((id, json) -> {
            Item item = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
            if (item == null) {
                VanillaBackport.LOGGER.error("Failed to load ghast equipment entry {}: no such item", id);
                return;
            }
            
            Entry.CODEC.parse(JsonOps.INSTANCE, json)
                .resultOrPartial(error -> VanillaBackport.LOGGER.error("Failed to parse ghast equipment entry {}: {}", id, error))
                .ifPresent(entry -> entries.put(item, entry.resolveTexture()));
        });
        
        DATADRIVEN_EQUIPMENT.clear();
        DATADRIVEN_EQUIPMENT.putAll(entries);
    }
    
    public record Entry(ResourceLocation texture) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(Entry::texture)
        ).apply(instance, Entry::new));
        
        public ResourceLocation resolveTexture() {
            return this.texture.withPath(path -> "textures/entity/" + path + ".png");
        }
    }
}