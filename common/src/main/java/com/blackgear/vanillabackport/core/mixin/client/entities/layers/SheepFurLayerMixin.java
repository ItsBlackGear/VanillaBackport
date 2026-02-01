package com.blackgear.vanillabackport.core.mixin.client.entities.layers;

import com.blackgear.vanillabackport.client.level.entities.model.BabySheepModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.core.ModConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepFurLayer.class)
public abstract class SheepFurLayerMixin extends RenderLayer<Sheep, SheepModel<Sheep>> {
    @Unique private static final ResourceLocation BABY_SHEEP_WOOL_TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_wool_baby.png");
    @Unique private BabySheepModel<Sheep> babyModel;

    public SheepFurLayerMixin(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer) {
        super(renderer);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer, EntityModelSet modelSet, CallbackInfo ci) {
        this.babyModel = new BabySheepModel<>(modelSet.bakeLayer(ModModelLayers.SHEEP_BABY));
    }

    @Inject(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onRender(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (sheep.isBaby() && !ModConstants.USE_LEGACY_BABY_MODELS) {
            if (!sheep.isSheared()) {
                if (sheep.isInvisible()) {
                    Minecraft minecraft = Minecraft.getInstance();
                    boolean bl = minecraft.shouldEntityAppearGlowing(sheep);
                    if (bl) {
                        this.getParentModel().copyPropertiesTo(this.babyModel);
                        this.babyModel.prepareMobModel(sheep, limbSwing, limbSwingAmount, partialTicks);
                        this.babyModel.setupAnim(sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.outline(BABY_SHEEP_WOOL_TEXTURE));
                        this.babyModel.renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(sheep, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
                    }
                } else {
                    float g;
                    float h;
                    float n;
                    if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
                        int j = sheep.tickCount / 25 + sheep.getId();
                        int k = DyeColor.values().length;
                        int l = j % k;
                        int m = (j + 1) % k;
                        float f = ((float)(sheep.tickCount % 25) + partialTicks) / 25.0F;
                        float[] fs = Sheep.getColorArray(DyeColor.byId(l));
                        float[] gs = Sheep.getColorArray(DyeColor.byId(m));
                        g = fs[0] * (1.0F - f) + gs[0] * f;
                        h = fs[1] * (1.0F - f) + gs[1] * f;
                        n = fs[2] * (1.0F - f) + gs[2] * f;
                    } else {
                        float[] hs = Sheep.getColorArray(sheep.getColor());
                        g = hs[0];
                        h = hs[1];
                        n = hs[2];
                    }

                    coloredCutoutModelCopyLayerRender(
                        this.getParentModel(),
                        this.babyModel,
                        BABY_SHEEP_WOOL_TEXTURE,
                        poseStack,
                        buffer,
                        packedLight,
                        sheep,
                        limbSwing,
                        limbSwingAmount,
                        ageInTicks,
                        netHeadYaw,
                        headPitch,
                        partialTicks,
                        g,
                        h,
                        n
                    );
                }
            }

            ci.cancel();
        }
    }
}