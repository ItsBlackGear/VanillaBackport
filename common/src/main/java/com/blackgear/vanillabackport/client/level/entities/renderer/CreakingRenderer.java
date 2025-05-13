package com.blackgear.vanillabackport.client.level.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.layer.CreakingEmissiveLayer;
import com.blackgear.vanillabackport.client.level.entities.model.CreakingModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.creaking.Creaking;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class CreakingRenderer<T extends Creaking> extends MobRenderer<T, CreakingModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(VanillaBackport.MOD_ID, "textures/entity/creaking/creaking.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = new ResourceLocation(VanillaBackport.MOD_ID, "textures/entity/creaking/creaking_eyes.png");

    public CreakingRenderer(EntityRendererProvider.Context context) {
        super(context, new CreakingModel<>(context.bakeLayer(ModModelLayers.CREAKING)), 0.7F);
        this.addLayer(new CreakingEmissiveLayer<>(this, EYES_TEXTURE_LOCATION, (creaking, partialTick, ageInTicks) -> 1.0F, CreakingModel::getHeadParts, RenderType::eyes));
    }

    @Override
    public ResourceLocation getTextureLocation(Creaking entity) {
        return TEXTURE_LOCATION;
    }
}