package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.client.api.modules.waypoints.WaypointStyle;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAsset;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAssets;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class WaypointStyleProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;
    
    public WaypointStyleProvider(FabricDataOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "waypoint_style");
    }
    
    private static void bootstrap(BiConsumer<ResourceKey<WaypointStyleAsset>, WaypointStyle> consumer) {
        consumer.accept(
            WaypointStyleAssets.DEFAULT,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("default_0"),
                    ResourceLocation.withDefaultNamespace("default_1"),
                    ResourceLocation.withDefaultNamespace("default_2"),
                    ResourceLocation.withDefaultNamespace("default_3")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.BOWTIE,
            new WaypointStyle(
                64,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("bowtie"),
                    ResourceLocation.withDefaultNamespace("default_0"),
                    ResourceLocation.withDefaultNamespace("default_1"),
                    ResourceLocation.withDefaultNamespace("default_2"),
                    ResourceLocation.withDefaultNamespace("default_3")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.LODESTONE,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("lodestone_0"),
                    ResourceLocation.withDefaultNamespace("lodestone_1"),
                    ResourceLocation.withDefaultNamespace("lodestone_2")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.DEATH,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("death_0"),
                    ResourceLocation.withDefaultNamespace("death_1"),
                    ResourceLocation.withDefaultNamespace("death_2")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.NORTH,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("north")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.SOUTH,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("south")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.WEST,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("west")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.EAST,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("east")
                )
            )
        );
        consumer.accept(
            WaypointStyleAssets.CORNER,
            new WaypointStyle(
                128,
                332,
                List.of(
                    ResourceLocation.withDefaultNamespace("corner")
                )
            )
        );
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<WaypointStyleAsset>, WaypointStyle> waypointStyles = new HashMap<>();
        bootstrap((id, asset) -> {
            if (waypointStyles.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register waypoint style twice for id: " + id);
            }
        });
        
        return save(cache, WaypointStyle.CODEC, o -> this.pathProvider.json(o.location()), waypointStyles);
    }
    
    private static <T, E> CompletableFuture<?> save(CachedOutput cache, Codec<E> codec, Function<T, Path> pathGetter, Map<T, E> contents) {
        return save(cache, e -> codec.encodeStart(JsonOps.INSTANCE, e).getOrThrow(), pathGetter, contents);
    }
    
    private static <T, E> CompletableFuture<?> save(CachedOutput cache, Function<E, JsonElement> serializer, Function<T, Path> pathGetter, Map<T, E> contents) {
        return CompletableFuture.allOf(contents.entrySet().stream().map(entry -> {
            Path path = pathGetter.apply(entry.getKey());
            JsonElement json = serializer.apply(entry.getValue());
            return DataProvider.saveStable(cache, json, path);
        }).toArray(CompletableFuture[]::new));
    }
    
    @Override
    public String getName() {
        return "Waypoint Style Definitions";
    }
}