package com.blackgear.vanillabackport.client.level.entities.layer;

import com.blackgear.vanillabackport.client.level.entities.model.CreakingModel;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CreakingEmissiveLayer<T extends Creaking, M extends CreakingModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;
    private final AlphaFunction<T> alphaFunction;
    private final DrawSelector<T, M> drawSelector;
    private final Function<ResourceLocation, RenderType> bufferProvider;

    public CreakingEmissiveLayer(
        RenderLayerParent<T, M> renderer,
        ResourceLocation texture,
        AlphaFunction<T> alphaFunction,
        DrawSelector<T, M> drawSelector,
        Function<ResourceLocation, RenderType> bufferProvider
    ) {
        super(renderer);
        this.texture = texture;
        this.alphaFunction = alphaFunction;
        this.drawSelector = drawSelector;
        this.bufferProvider = bufferProvider;
    }

    public void render(
        PoseStack poseStack,
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
        if (!entity.isInvisible() && this.onlyDrawSelectedParts()) {
            VertexConsumer vertices = buffer.getBuffer(this.bufferProvider.apply(this.texture));
            float alpha = this.alphaFunction.apply(entity, partialTick, ageInTicks);
            int color = FastColor.ARGB32.color(Mth.floor(alpha * 255.0F), 255, 255, 255);
            this.getParentModel()
                .renderToBuffer(
                    poseStack,
                    vertices,
                    packedLight,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                    color
                );
            this.resetDrawForAllParts();
        }
    }

    private boolean onlyDrawSelectedParts() {
        List<ModelPart> partsToDraw = this.drawSelector.getPartsToDraw(this.getParentModel());

        if (partsToDraw.isEmpty()) {
            return false;
        } else {
            this.getParentModel().root().getAllParts().forEach(model -> model.skipDraw = true);
            partsToDraw.forEach(model -> model.skipDraw = false);
            return true;
        }
    }

    private void resetDrawForAllParts() {
        this.getParentModel().root().getAllParts().forEach(model -> model.skipDraw = false);
    }

    @Environment(EnvType.CLIENT)
    public interface AlphaFunction<T extends Creaking> {
        float apply(T creaking, float partialTick, float ageInTicks);
    }

    @Environment(EnvType.CLIENT)
    public interface DrawSelector<T extends Creaking, M extends EntityModel<T>> {
        List<ModelPart> getPartsToDraw(M model);
    }
}