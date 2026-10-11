package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.client.v2.render.DynamicItemRenderer;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class DyePaletteRenderer implements DynamicItemRenderer.Renderer {
    public static final DyePaletteRenderer INSTANCE = new DyePaletteRenderer();
    public static final ModelResourceLocation DYE_PALETTE = new ModelResourceLocation(VanillaBackport.resource("dye_palette"), "inventory");
    public static final List<Item> DYES = List.of(
        Items.WHITE_DYE, Items.LIGHT_GRAY_DYE, Items.GRAY_DYE, Items.BLACK_DYE,
        Items.BROWN_DYE, Items.RED_DYE, Items.ORANGE_DYE, Items.YELLOW_DYE,
        Items.LIME_DYE, Items.GREEN_DYE, Items.CYAN_DYE, Items.LIGHT_BLUE_DYE,
        Items.BLUE_DYE, Items.PURPLE_DYE, Items.MAGENTA_DYE, Items.PINK_DYE
    );
    private static final Map<Item, ModelResourceLocation> DYE_MODELS = DYES.stream()
        .collect(Collectors.toUnmodifiableMap(
            item -> item,
            item -> new ModelResourceLocation(VanillaBackport.resource(BuiltInRegistries.ITEM.getKey(item).getPath()), "inventory")
        ));
    private static final Set<ModelResourceLocation> ALL_MODELS = Stream.concat(
        DYE_MODELS.values().stream(),
        Stream.of(DYE_PALETTE)
    ).collect(Collectors.toUnmodifiableSet());
    
    @Override
    public void renderFirstPerson(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, int overlay, BakedModel model, ItemModelShaper shaper, ItemColors colors) {
        if (stack.getItem() instanceof DyeItem item) {
            boolean isPaletteEnabled = VanillaBackport.CLIENT_CONFIG.enableDyePalettes.get();
            boolean isModernRenderer = VanillaBackport.CLIENT_CONFIG.enableModernDyeTextures.get();
            
            ModelResourceLocation location = DYE_MODELS.get(item);
            BakedModel dye = isModernRenderer && location != null
                ? shaper.getModelManager().getModel(location)
                : model;
            
            dye.getTransforms().getTransform(context).apply(leftHand, pose);
            pose.translate(-0.5F, -0.5F, -0.5F);
            
            RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, true);
            VertexConsumer vertices = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil());
            
            if (isPaletteEnabled) {
                BakedModel palette = shaper.getModelManager().getModel(DYE_PALETTE);
                this.renderModelLists(palette, stack, light, overlay, pose, vertices, colors);
            }
            
            this.renderModelLists(dye, stack, light, overlay, pose, vertices, colors);
        }
    }
    
    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.pass();
    }
    
    @Override
    public boolean shouldUse() {
        return VanillaBackport.CLIENT_CONFIG.enableDyePalettes.get() || VanillaBackport.CLIENT_CONFIG.enableModernDyeTextures.get();
    }
    
    @Override
    public Set<ModelResourceLocation> registerModels() {
        return ALL_MODELS;
    }
}