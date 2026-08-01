package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.api.modules.emissive_models.LazyModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public class SheepWoolUndercoatLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {
    private static final ResourceLocation SHEEP_WOOL_UNDERCOAT_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/sheep/sheep_wool_undercoat.png");
    private final LazyModel<Sheep, EntityModel<Sheep>> model;

    public SheepWoolUndercoatLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> renderer, EntityModelSet models) {
        super(renderer);
        this.model = LazyModel.of(models, ModelLayers.SHEEP, SheepFurModel::new);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (sheep.getType() == EntityType.SHEEP && !sheep.isInvisible()) {
            int color;
            if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
                int offset = sheep.tickCount / 25 + sheep.getId();
                int size = DyeColor.values().length;
                int fromIndex = offset % size;
                int toIndex = (offset + 1) % size;
                float delta = ((float)(sheep.tickCount % 25) + partialTick) / 25.0F;
                int fromColor = Sheep.getColor(DyeColor.byId(fromIndex));
                int toColor = Sheep.getColor(DyeColor.byId(toIndex));
                color = FastColor.ARGB32.lerp(delta, fromColor, toColor);
            } else {
                color = Sheep.getColor(sheep.getColor());
            }

            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model.get(), SHEEP_WOOL_UNDERCOAT_TEXTURE, poseStack, buffer, packedLight, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick, color);
        }
    }
}
