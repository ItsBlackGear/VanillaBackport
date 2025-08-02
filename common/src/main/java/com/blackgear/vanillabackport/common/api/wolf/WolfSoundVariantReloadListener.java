package com.blackgear.vanillabackport.common.api.wolf;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class WolfSoundVariantReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String DIRECTORY = "wolf_sound_variant";
    public static final WolfSoundVariantReloadListener INSTANCE = new WolfSoundVariantReloadListener();

    public WolfSoundVariantReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        profiler.push("Loading wolf sound variants");

        ModBuiltinRegistries.WOLF_SOUND_VARIANTS.clearDataDrivenEntries();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation name = entry.getKey();

            try {
                WolfSoundVariant.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(error -> VanillaBackport.LOGGER.error("Failed to parse wolf sound variant {}: {}", name, error))
                    .ifPresent(variant -> ModBuiltinRegistries.WOLF_SOUND_VARIANTS.registerDataDriven(name, variant));
            } catch (JsonParseException exception) {
                VanillaBackport.LOGGER.error("Failed to parse wolf sound variant JSON {}: {}", name, exception.getMessage(), exception);
            } catch (Exception exception) {
                VanillaBackport.LOGGER.error("Unexpected error processing wolf sound variant {}", name, exception);
            }
        }

        profiler.pop();
    }
}