package com.blackgear.vanillabackport.common.api.variant.spawn;

import com.blackgear.platform.core.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public class ClientsideSpawnPrioritySelector {
    public static HolderSet<Biome> getRequiredBiomes(TagKey<Biome> biome) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            return minecraft.level.registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        }

        return Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
    }

    public static Holder<Biome> getRequiredBiomes(ResourceKey<Biome> biome) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            return minecraft.level.registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
        }

        return Environment.getCurrentServer().get().registryAccess().lookup(Registries.BIOME).get().getOrThrow(biome);
    }

    public static Optional<RegistryAccess> deferred() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            return Optional.of(minecraft.level.registryAccess());
        }

        return Optional.empty();
    }
}