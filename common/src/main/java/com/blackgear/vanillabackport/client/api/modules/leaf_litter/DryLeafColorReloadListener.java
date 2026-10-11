package com.blackgear.vanillabackport.client.api.modules.leaf_litter;

import com.blackgear.platform.common.resource.RegistryAwareJsonReloadListener;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.biome.Biome;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DryLeafColorReloadListener extends RegistryAwareJsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final List<Entry> ENTRIES = new CopyOnWriteArrayList<>();
    public static final DryLeafColorReloadListener INSTANCE = new DryLeafColorReloadListener();
    
    public DryLeafColorReloadListener() {
        super(GSON, "dry_foliage_colors");
    }
    
    @Override
    public void parse(Map<ResourceLocation, JsonElement> map, RegistryAccess registryAccess, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
        
        List<Entry> loadedEntries = map.entrySet().stream()
            .map(entry -> Entry.CODEC.parse(ops, entry.getValue())
                .resultOrPartial(error -> VanillaBackport.LOGGER.error("Failed to parse dry foliage color {}: {}", entry.getKey(), error))
            )
            .flatMap(Optional::stream)
            .sorted(Comparator.comparingInt(Entry::priority).reversed())
            .toList();
        
        ENTRIES.clear();
        ENTRIES.addAll(loadedEntries);
        
        if (!ENTRIES.isEmpty()) {
            VanillaBackport.LOGGER.info("Loaded {} custom dry foliage color rule(s)", ENTRIES.size());
        }
    }
    
    public static OptionalInt getColorForBiome(Holder<Biome> biome) {
        for (Entry entry : ENTRIES) {
            if (entry.biomes().contains(biome)) {
                return OptionalInt.of(entry.color());
            }
        }
        
        return OptionalInt.empty();
    }
    
    public record Entry(HolderSet<Biome> biomes, int color, int priority) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biome").forGetter(Entry::biomes),
            Codec.INT.fieldOf("color").forGetter(Entry::color),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(Entry::priority)
        ).apply(instance, Entry::new));
    }
}