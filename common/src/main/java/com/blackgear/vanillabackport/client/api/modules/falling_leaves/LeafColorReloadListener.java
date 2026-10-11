package com.blackgear.vanillabackport.client.api.modules.falling_leaves;

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
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class LeafColorReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<Block, Properties> CUSTOM_COLORS = new ConcurrentHashMap<>();
    public static final LeafColorReloadListener INSTANCE = new LeafColorReloadListener();

    public LeafColorReloadListener() {
        super(GSON, "leaf_colors");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<Block, Properties> colors = resources.entrySet().stream()
            .map(entry -> Entry.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                .resultOrPartial(error -> VanillaBackport.LOGGER.error("Failed to parse leaf color {}: {}", entry.getKey(), error))
            )
            .flatMap(Optional::stream)
            .collect(Collectors.toMap(
                Entry::block,
                Entry::properties,
                (existing, replacement) -> replacement.priority() > existing.priority() ? replacement : existing
            ));
        
        CUSTOM_COLORS.clear();
        CUSTOM_COLORS.putAll(colors);
        
        if (!CUSTOM_COLORS.isEmpty()) {
            VanillaBackport.LOGGER.info("Loaded {} custom leaf color(s)", CUSTOM_COLORS.size());
        }
    }
    
    public static int getCustomColor(Block block) {
        Properties entry = CUSTOM_COLORS.get(block);
        return entry != null ? entry.color() : 0;
    }
    
    public static boolean hasCustomColor(Block block) {
        return CUSTOM_COLORS.containsKey(block);
    }
    
    public record Properties(int color, int priority) {
        public static final Codec<Properties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(Properties::color),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(Properties::priority)
        ).apply(instance, Properties::new));
    }
    
    public record Entry(Block block, Properties properties) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Entry::block),
            Properties.CODEC.fieldOf("properties").forGetter(Entry::properties)
        ).apply(instance, Entry::new));
    }
}