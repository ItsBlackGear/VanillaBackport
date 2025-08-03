package com.blackgear.vanillabackport.core.data;

import com.blackgear.platform.common.data.DataTransformer;
import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DataTransformation {
    public static final List<String> PARTICLE_IDS = List.of(
        "pale_oak_leaves",
        "trail"
    );
    public static final List<String> BLOCK_IDS = List.of(
        "pale_oak_leaves",
        "pale_oak_planks",
        "pale_oak_stairs",
        "pale_oak_slab",
        "pale_oak_fence",
        "pale_oak_fence_gate",
        "pale_oak_door",
        "pale_oak_wood",
        "pale_oak_log",
        "stripped_pale_oak_wood",
        "stripped_pale_oak_log",
        "pale_moss_block",
        "pale_moss_carpet",
        "pale_hanging_moss",
        "open_eyeblossom",
        "closed_eyeblossom",
        "pale_oak_sapling",
        "potted_open_eyeblossom",
        "potted_closed_eyeblossom",
        "potted_pale_oak_sapling",
        "creaking_heart",
        "pale_oak_sign",
        "pale_oak_wall_sign",
        "pale_oak_hanging_sign",
        "pale_oak_wall_hanging_sign",
        "pale_oak_pressure_plate",
        "pale_oak_trapdoor",
        "resin_clump",
        "resin_block",
        "resin_bricks",
        "resin_brick_stairs",
        "resin_brick_slab",
        "resin_brick_wall",
        "chiseled_resin_bricks",
        "dried_ghast"
    );
    public static final List<String> ITEM_IDS = List.of(
        "resin_brick",
        "pale_oak_boat",
        "pale_oak_chest_boat",
        "creaking_spawn_egg",
        "happy_ghast_spawn_egg",
        "white_harness",
        "orange_harness",
        "magenta_harness",
        "light_blue_harness",
        "yellow_harness",
        "lime_harness",
        "pink_harness",
        "gray_harness",
        "light_gray_harness",
        "cyan_harness",
        "purple_harness",
        "blue_harness",
        "brown_harness",
        "black_harness",
        "music_disc_tears",
        "music_disc_lava_chicken"
    );
    public static final List<String> BLOCK_ENTITY_IDS = List.of(
        "creaking_heart"
    );
    public static final List<String> ENTITY_IDS = List.of(
        "creaking",
        "happy_ghast",
        "pale_oak_boat",
        "pale_oak_chest_boat"
    );
    public static final List<String> JUKEBOX_SONG_IDS = List.of(
        "tears",
        "lava_chicken"
    );
    public static final List<String> PAINTING_VARIANT_IDS = List.of(
        "dennis"
    );
    public static final List<String> BIOME_IDS = List.of(
        "pale_garden"
    );
    public static final List<String> TRIM_MATERIAL_IDS = List.of(
        "resin"
    );
    public static final List<String> BLOCK_TAG_IDS = List.of(
        "pale_oak_logs",
        "happy_ghast_avoids"
    );
    public static final List<String> ITEM_TAG_IDS = List.of(
        "pale_oak_logs",
        "happy_ghast_tempt_items",
        "happy_ghast_food",
        "harnesses"
    );
    public static final List<String> ENTITY_TAG_IDS = List.of(
        "followable_friendly_mobs"
    );

    public static void bootstrap() {
        DataTransformer.onDataTransformation(transformer -> {
            String namespace = VanillaBackport.MOD_ID;
            PARTICLE_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_IDS.forEach(id -> remap(transformer, namespace, id));
            ITEM_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_ENTITY_IDS.forEach(id -> remap(transformer, namespace, id));
            ENTITY_IDS.forEach(id -> remap(transformer, namespace, id));
            JUKEBOX_SONG_IDS.forEach(id -> remap(transformer, namespace, id));
            PAINTING_VARIANT_IDS.forEach(id -> remap(transformer, namespace, id));
            BIOME_IDS.forEach(id -> remap(transformer, namespace, id));
            TRIM_MATERIAL_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_TAG_IDS.forEach(id -> remap(transformer, namespace, id));
            ITEM_TAG_IDS.forEach(id -> remap(transformer, namespace, id));
            ENTITY_TAG_IDS.forEach(id -> remap(transformer, namespace, id));

            String everycompat = "everycomp";
            if (Environment.hasModLoaded(everycompat)) {
                transformer.add(original -> {
                    String path = original.getPath();
                    if (path.contains(namespace)) {
                        String newPath = path.replaceAll(namespace, VanillaBackport.NAMESPACE);
                        return new ResourceLocation(original.getNamespace(), newPath);
                    }

                    return null;
                });
            }
        });
    }

    private static void remap(DataTransformer.Transformer transformer, String namespace, String id) {
        transformer.remap(new ResourceLocation(namespace, id), VanillaBackport.vanilla(id));
    }
}