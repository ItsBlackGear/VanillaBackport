package com.blackgear.vanillabackport.client.level.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.model.ArmadilloModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.armadillo.Armadillo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ArmadilloRenderer extends MobRenderer<Armadillo, ArmadilloModel> {
	private static final ResourceLocation ARMADILLO_LOCATION = new ResourceLocation("textures/entity/armadillo.png");

	public ArmadilloRenderer(EntityRendererProvider.Context context) {
		super(context, new ArmadilloModel(context.bakeLayer(ModModelLayers.ARMADILLO)), 0.4F);
	}

	@Override
	public ResourceLocation getTextureLocation(Armadillo armadillo) {
		return ARMADILLO_LOCATION;
	}
}