package com.blackgear.vanillabackport.common.resource;

import com.blackgear.platform.common.resource.RegistryAwareJsonReloadListener;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class PigVariantReloadListener extends RegistryAwareJsonReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final String DIRECTORY = "pig_variant";

    public PigVariantReloadListener() {
        super(GSON, DIRECTORY);
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> resources, RegistryAccess registryAccess, ResourceManager manager, ProfilerFiller profiler) {
        profiler.push("Loading pig variants");

        DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation name = entry.getKey();
            JsonElement element = entry.getValue();

            try {
                PigVariant.CODEC.parse(ops, element)
                    .resultOrPartial(error -> VanillaBackport.LOGGER.error("Failed to parse pig variant {}: {}", name, error))
                    .ifPresent(variant -> ModBuiltinRegistries.PIG_VARIANTS.register(name, variant));
            } catch (JsonParseException exception) {
                VanillaBackport.LOGGER.error("Failed to parse pig variant JSON {}: {}", name, exception.getMessage(), exception);
            } catch (Exception exception) {
                VanillaBackport.LOGGER.error("Unexpected error processing pig variant {}", name, exception);
            }
        }

        profiler.pop();
    }
}