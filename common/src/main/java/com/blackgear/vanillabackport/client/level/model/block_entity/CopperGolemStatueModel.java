package com.blackgear.vanillabackport.client.level.model.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueModel extends Model {
    private final ModelPart root;

    public CopperGolemStatueModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(pose, buffer, light, overlay, red, green, blue, alpha);
    }
    
    public void setupAnim(Direction direction) {
        this.root.y = 0.0F;
        this.root.yRot = direction.getOpposite().toYRot() * (float) (Math.PI / 180.0);
        this.root.zRot = (float) Math.PI;
    }
}