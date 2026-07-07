package com.blackgear.vanillabackport.client.level.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class BlockDecorationLayer<S extends Entity, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private final BlockRenderDispatcher dispatcher;
    private final Function<S, BlockState> blockModel;
    private final Consumer<PoseStack> transform;
    
    public BlockDecorationLayer(RenderLayerParent<S, M> renderer, BlockRenderDispatcher dispatcher, Function<S, BlockState> blockModel, Consumer<PoseStack> transform) {
        super(renderer);
        this.dispatcher = dispatcher;
        this.blockModel = blockModel;
        this.transform = transform;
    }
    
    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, S entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState shaper = this.blockModel.apply(entity);
        if (shaper != null) {
            stack.pushPose();
            this.transform.accept(stack);
            stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            stack.scale(1.0F, 1.0F, 1.0F);
            stack.translate(-0.5F, -1.45F, -0.5F);
            this.dispatcher.renderSingleBlock(shaper, stack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }
    }
}
