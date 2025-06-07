package com.blackgear.vanillabackport.client.level.entities.renderer;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.WaterPatchModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.vehicle.Boat;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class PaleOakBoatRenderer extends BoatRenderer {
    private static final ResourceLocation PALE_OAK_BOAT = VanillaBackport.resource("textures/entity/boat/pale_oak.png");
    private static final ResourceLocation PALE_OAK_CHEST_BOAT = VanillaBackport.resource("textures/entity/chest_boat/pale_oak.png");
    private final Pair<ResourceLocation, ListModel<Boat>> boatResource;

    public PaleOakBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context, chestBoat);
        this.boatResource = Pair.of(chestBoat ? PALE_OAK_CHEST_BOAT : PALE_OAK_BOAT, chestBoat ? new ChestBoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_CHEST_BOAT)) : new BoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_BOAT)));
    }

    @Override
    public void render(Boat entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource buffer, int packedLight) {
        matrices.pushPose();
        matrices.translate(0.0F, 0.375F, 0.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        float hurtTime = (float)entity.getHurtTime() - partialTick;
        float tilt = entity.getDamage() - partialTick;
        if (tilt < 0.0F) {
            tilt = 0.0F;
        }

        if (hurtTime > 0.0F) {
            matrices.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * tilt / 10.0F * (float)entity.getHurtDir()));
        }

        float bubbleAngle = entity.getBubbleAngle(partialTick);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            matrices.mulPose((new Quaternionf()).setAngleAxis(entity.getBubbleAngle(partialTick) * ((float)Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        }

        Pair<ResourceLocation, ListModel<Boat>> pair = this.getModelWithLocation(entity);
        ResourceLocation texture = pair.getFirst();
        ListModel<Boat> model = pair.getSecond();
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.mulPose(Axis.YP.rotationDegrees(90.0F));
        model.setupAnim(entity, partialTick, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertices = buffer.getBuffer(model.renderType(texture));
        model.renderToBuffer(matrices, vertices, packedLight, OverlayTexture.NO_OVERLAY);

        if (!entity.isUnderWater()) {
            VertexConsumer patch = buffer.getBuffer(RenderType.waterMask());
            if (model instanceof WaterPatchModel waterpatchmodel) {
                waterpatchmodel.waterPatch().render(matrices, patch, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }

        matrices.popPose();

        Entity leashHolder = entity.getLeashHolder();
        if (leashHolder != null) {
            this.renderLeash(entity, partialTick, matrices, buffer, leashHolder);
        }

        if (this.shouldShowName(entity)) {
            this.renderNameTag(entity, entity.getDisplayName(), matrices, buffer, packedLight, partialTick);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Boat entity) {
        return this.getModelWithLocation(entity).getFirst();
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return this.boatResource;
    }
}