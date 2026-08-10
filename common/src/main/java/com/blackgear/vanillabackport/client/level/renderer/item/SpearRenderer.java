package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.client.v2.render.ItemRendererRegistry;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class SpearRenderer implements ItemRendererRegistry.Renderer {
    public static final SpearRenderer INSTANCE = new SpearRenderer();
    public static final Set<ItemLike> SPEARS = Set.of(
        ModItems.WOODEN_SPEAR.get(),
        ModItems.STONE_SPEAR.get(),
        ModItems.COPPER_SPEAR.get(),
        ModItems.IRON_SPEAR.get(),
        ModItems.GOLDEN_SPEAR.get(),
        ModItems.DIAMOND_SPEAR.get(),
        ModItems.NETHERITE_SPEAR.get()
    );
    
    private static final Map<ItemLike, ModelResourceLocation> SPEAR_MODELS = buildModels();
    
    private static Map<ItemLike, ModelResourceLocation> buildModels() {
        Map<ItemLike, ModelResourceLocation> models = new HashMap<>(SPEARS.size());
        for (ItemLike item : SPEARS) models.put(item, create(item.asItem()));
        return models;
    }
    
    private static ModelResourceLocation create(Item item) {
        return new ModelResourceLocation(new ResourceLocation(BuiltInRegistries.ITEM.getKey(item).getPath() + "_in_hand"), "inventory");
    }
    
    @Override
    public ResultHolder<BakedModel> renderFirstPerson(ItemStack stack, ItemDisplayContext context, ItemModelShaper shaper) {
        return context == ItemDisplayContext.GUI || context == ItemDisplayContext.FIXED || context == ItemDisplayContext.GROUND
            ? ResultHolder.pass()
            : ResultHolder.submit(shaper.getModelManager().getModel(SPEAR_MODELS.get(stack.getItem())));
    }
    
    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.pass();
    }
    
    @Override
    public Set<ModelResourceLocation> registerModels() {
        return ImmutableSet.copyOf(SPEAR_MODELS.values());
    }
}