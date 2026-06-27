package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.platform.client.v2.render.DynamicItemRenderer;
import com.blackgear.platform.client.v2.render.ItemRendererRegistry;
import com.blackgear.vanillabackport.client.level.item.BundleRenderer;
import com.blackgear.vanillabackport.client.level.item.SpawnEggRenderer;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.ItemLike;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class ItemLikeRendering {
    public static void specialRendering(SpecialModelEvent event) {
        for (ItemLike item : BundleRenderer.BUNDLES) {
            ItemRendererRegistry.INSTANCE.get().register(item, BundleRenderer.INSTANCE);
        }
        
        for (ItemLike item : SpawnEggRenderer.SPAWN_EGGS) {
            DynamicItemRenderer.INSTANCE.get().register(item, SpawnEggRenderer.INSTANCE);
        }
    }
    
    public static void renderTypes(BlockRendererEvent event) {
        event.register(
            RenderType.cutoutMipped(),
            ModBlocks.PALE_OAK_LEAVES.get()
        );
        event.register(
            RenderType.cutout(),
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.PALE_HANGING_MOSS.get(),
            ModBlocks.OPEN_EYEBLOSSOM.get(),
            ModBlocks.CLOSED_EYEBLOSSOM.get(),
            ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(),
            ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(),
            ModBlocks.PALE_OAK_SAPLING.get(),
            ModBlocks.POTTED_PALE_OAK_SAPLING.get(),
            ModBlocks.RESIN_CLUMP.get(),
            ModBlocks.BUSH.get(),
            ModBlocks.FIREFLY_BUSH.get(),
            ModBlocks.WILDFLOWERS.get(),
            ModBlocks.LEAF_LITTER.get(),
            ModBlocks.CACTUS_FLOWER.get(),
            ModBlocks.SHORT_DRY_GRASS.get(),
            ModBlocks.TALL_DRY_GRASS.get(),
            ModBlocks.PALE_OAK_DOOR.get(),
            ModBlocks.PALE_OAK_TRAPDOOR.get(),
            ModBlocks.SULFUR_SPIKE.get()
        );
    }
}