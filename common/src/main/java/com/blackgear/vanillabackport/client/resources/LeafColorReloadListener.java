package com.blackgear.vanillabackport.client.resources;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        // First, collect all entries for each block
        Map<Block, Map<ResourceLocation, Properties>> blockEntries = new HashMap<>();
        int skippedCount = 0;

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            JsonElement element = entry.getValue();

            if (!element.isJsonObject()) continue;

            JsonObject object = element.getAsJsonObject();
            if (!object.has("block") || !object.has("properties")) {
                skippedCount++;
                continue;
            }

            String blockId = object.get("block").getAsString();
            ResourceLocation blockLocation = ResourceLocation.tryParse(blockId);

            if (blockLocation == null || !BuiltInRegistries.BLOCK.containsKey(blockLocation)) {
                skippedCount++;
                continue;
            }

            JsonObject properties = object.getAsJsonObject("properties");
            if (!properties.has("color")) {
                skippedCount++;
                continue;
            }

            Block block = BuiltInRegistries.BLOCK.get(blockLocation);
            int color = properties.get("color").getAsInt();
            int priority = properties.has("priority") ? properties.get("priority").getAsInt() : 0;

            blockEntries.computeIfAbsent(block, k -> new HashMap<>())
                .put(entry.getKey(), new Properties(color, priority));
        }

        CUSTOM_COLORS.clear();

        // Process each block and ensure the highest priority wins
        for (Map.Entry<Block, Map<ResourceLocation, Properties>> blockEntry : blockEntries.entrySet()) {
            Block block = blockEntry.getKey();
            Properties selectedEntry = getColorProperties(blockEntry);

            if (selectedEntry != null) {
                CUSTOM_COLORS.put(block, selectedEntry);
            }
        }

        // Double-check: verify no lower priority entries overrode higher ones
        for (Map.Entry<Block, Properties> finalEntry : CUSTOM_COLORS.entrySet()) {
            Block block = finalEntry.getKey();
            Properties finalProps = finalEntry.getValue();
            Map<ResourceLocation, Properties> allEntries = blockEntries.get(block);

            if (allEntries != null && allEntries.size() > 1) {
                int maxPriority = allEntries.values().stream()
                    .mapToInt(p -> p.priority)
                    .max()
                    .orElse(0);

                if (finalProps.priority != maxPriority) {
                    // Force correct the entry
                    Properties correctEntry = allEntries.values().stream()
                        .filter(p -> p.priority == maxPriority)
                        .findFirst()
                        .orElse(finalProps);

                    CUSTOM_COLORS.put(block, correctEntry);
                }
            }
        }

        if (skippedCount > 0) {
            VanillaBackport.LOGGER.info("Skipping {} leaf color(s).", skippedCount);
        }

        if (!CUSTOM_COLORS.isEmpty()) {
            VanillaBackport.LOGGER.info("Loaded {} custom leaf colors: {}",
                CUSTOM_COLORS.size(),
                CUSTOM_COLORS.keySet().stream()
                    .map(block -> BuiltInRegistries.BLOCK.getKey(block).toString())
                    .sorted()
                    .toList());
        }
    }

    private static @Nullable Properties getColorProperties(Map.Entry<Block, Map<ResourceLocation, Properties>> blockEntry) {
        Map<ResourceLocation, Properties> entries = blockEntry.getValue();

        // Find the entry with the highest priority
        Properties selectedEntry = null;
        int highestPriority = Integer.MIN_VALUE;

        for (Map.Entry<ResourceLocation, Properties> entry : entries.entrySet()) {
            Properties props = entry.getValue();
            if (props.priority > highestPriority) {
                highestPriority = props.priority;
                selectedEntry = props;
            }
        }
        return selectedEntry;
    }

    public static int getCustomColor(Block block) {
        Properties entry = CUSTOM_COLORS.get(block);
        return entry != null ? entry.color : 0;
    }

    public static boolean hasCustomColor(Block block) {
        return CUSTOM_COLORS.containsKey(block);
    }

    private record Properties(int color, int priority) {
        // NO-OP
    }
}