package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.api.modules.models.LazyModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class UndeadHorseArmorLayer extends RenderLayer<AbstractHorse, HorseModel<AbstractHorse>> {
	private final LazyModel<AbstractHorse, HorseModel<AbstractHorse>> model;

	public UndeadHorseArmorLayer(RenderLayerParent<AbstractHorse, HorseModel<AbstractHorse>> renderer, EntityModelSet models) {
		super(renderer);
		this.model = LazyModel.of(models, ModModelLayers.UNDEAD_HORSE_ARMOR, HorseModel::new);
	}

	@Override
	public void render(
		PoseStack poseStack,
		MultiBufferSource buffer,
		int packedLight,
		AbstractHorse entity,
		float limbSwing,
		float limbSwingAmount,
		float partialTicks,
		float ageInTicks,
		float netHeadYaw,
		float headPitch
	) {
		ItemStack equipment = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (equipment.getItem() instanceof HorseArmorItem armor) {
            this.getParentModel().copyPropertiesTo(this.model.get());
			this.model.get().prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
			this.model.get().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			float r = 1.0F;
			float g = 1.0F;
			float b = 1.0F;
			if (armor instanceof DyeableHorseArmorItem dyeable) {
				int color = dyeable.getColor(equipment);
				r = (float) (color >> 16 & 0xFF) / 255.0F;
				g = (float) (color >> 8 & 0xFF) / 255.0F;
				b = (float) (color & 0xFF) / 255.0F;
			}
			
			VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(armor.getTexture()));
			this.model.get().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
		}
	}
}
