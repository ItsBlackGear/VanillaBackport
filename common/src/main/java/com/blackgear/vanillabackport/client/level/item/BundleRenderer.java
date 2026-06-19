package com.blackgear.vanillabackport.client.level.item;

import com.blackgear.platform.client.v2.render.ItemRendererRegistry;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BundleRenderer implements ItemRendererRegistry.Renderer {
    public static final Set<ItemLike> BUNDLES = Set.of(
        Items.BUNDLE,
        ModItems.WHITE_BUNDLE.get(),
        ModItems.ORANGE_BUNDLE.get(),
        ModItems.MAGENTA_BUNDLE.get(),
        ModItems.LIGHT_BLUE_BUNDLE.get(),
        ModItems.YELLOW_BUNDLE.get(),
        ModItems.LIME_BUNDLE.get(),
        ModItems.PINK_BUNDLE.get(),
        ModItems.GRAY_BUNDLE.get(),
        ModItems.LIGHT_GRAY_BUNDLE.get(),
        ModItems.CYAN_BUNDLE.get(),
        ModItems.BLUE_BUNDLE.get(),
        ModItems.BROWN_BUNDLE.get(),
        ModItems.GREEN_BUNDLE.get(),
        ModItems.RED_BUNDLE.get(),
        ModItems.BLACK_BUNDLE.get(),
        ModItems.PURPLE_BUNDLE.get()
    );

    private static final Map<ItemLike, ModelResourceLocation> BUNDLE_MODELS = buildModels();

    private static Map<ItemLike, ModelResourceLocation> buildModels() {
        Map<ItemLike, ModelResourceLocation> models = new HashMap<>();
        for (ItemLike item : BUNDLES) models.put(item, create(item.asItem()));
        return models;
    }

    private static ModelResourceLocation create(Item item) {
        return new ModelResourceLocation(VanillaBackport.resource(BuiltInRegistries.ITEM.getKey(item).getPath()), "inventory");
    }

    @Override
    public boolean shouldUse() {
        return VanillaBackport.COMMON_CONFIG.hasModernBundleGraphics.get();
    }

    @Override
    public ResultHolder<BakedModel> renderFirstPerson(ItemStack stack, ItemDisplayContext context, ItemModelShaper shaper) {
        return ResultHolder.submit(shaper.getModelManager().getModel(BUNDLE_MODELS.get(stack.getItem())));
    }

    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.submit(shaper.getModelManager().getModel(BUNDLE_MODELS.get(stack.getItem())));
    }

    @Override
    public Set<ModelResourceLocation> registerModels() {
        Set<ModelResourceLocation> models = ImmutableSet.of();
        models = ImmutableSet.<ModelResourceLocation>builder()
            .addAll(models)
            .addAll(BUNDLE_MODELS.values())
            .build();
        return models;
    }
}