package com.blackgear.vanillabackport.client.level.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class LivingEntityEmissiveLayer<T extends LivingEntity, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {
    private final Function<T, ResourceLocation> textureProvider;
    private final AlphaFunction<T> alphaFunction;
    private final M model;
    private final Function<ResourceLocation, RenderType> bufferProvider;
    private final boolean alwaysVisible;

    public LivingEntityEmissiveLayer(
        RenderLayerParent<T, M> renderer,
        Function<T, ResourceLocation> textureProvider,
        AlphaFunction<T> alphaFunction,
        M model,
        Function<ResourceLocation, RenderType> bufferProvider,
        boolean alwaysVisible
    ) {
        super(renderer);
        this.textureProvider = textureProvider;
        this.alphaFunction = alphaFunction;
        this.model = model;
        this.bufferProvider = bufferProvider;
        this.alwaysVisible = alwaysVisible;
    }

    @Override
    public void render(
        PoseStack pose,
        MultiBufferSource buffer,
        int packedLight,
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        if (!entity.isInvisible() || this.alwaysVisible) {
            float alpha = this.alphaFunction.apply(entity, ageInTicks);
            if (alpha > Mth.EPSILON) {
                RenderType renderType = this.bufferProvider.apply(this.textureProvider.apply(entity));
                int color = FastColor.ARGB32.color(Mth.floor(alpha * 255.0F), 255, 255, 255);
                this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.model.renderToBuffer(pose, buffer.getBuffer(renderType), packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), color);
                this.getParentModel().copyPropertiesTo(this.model);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public interface AlphaFunction<T extends LivingEntity> {
        float apply(T entity, float ageInTicks);
    }
}