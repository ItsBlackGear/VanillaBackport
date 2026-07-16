package com.blackgear.vanillabackport.client.level.layer;

import com.blackgear.vanillabackport.client.api.modules.emissive_models.LazyModel;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

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
		ItemStack equipment = entity.getBodyArmorItem();
		if (equipment.getItem() instanceof AnimalArmorItem armor && armor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN) {
			this.getParentModel().copyPropertiesTo(this.model.get());
			this.model.get().prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
			this.model.get().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			int color = equipment.is(ItemTags.DYEABLE) ? ARGB32.opaque(DyedItemColor.getOrDefault(equipment, -6265536)) : -1;
			
			VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(armor.getTexture()));
			this.model.get().renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, color);
		}
	}
}
