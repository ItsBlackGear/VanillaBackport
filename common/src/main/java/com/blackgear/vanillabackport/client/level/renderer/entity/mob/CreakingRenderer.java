package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.LivingEntityEmissiveLayer;
import com.blackgear.vanillabackport.client.level.model.entity.CreakingModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.monster.creaking.Creaking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class CreakingRenderer<T extends Creaking> extends MobRenderer<T, CreakingModel<T>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png");

    public CreakingRenderer(EntityRendererProvider.Context context) {
        super(context, new CreakingModel<>(context.bakeLayer(ModModelLayers.CREAKING)), 0.7F);
        this.addLayer(new LivingEntityEmissiveLayer<>(
            this,
            creaking -> EYES_TEXTURE_LOCATION,
            (creaking, ageInTicks) -> creaking.shouldEyesGlow() ? 1.0F : 0.0F,
            new CreakingModel<>(context.bakeLayer(ModModelLayers.CREAKING)),
            RenderType::eyes,
            true
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(Creaking entity) {
        return TEXTURE_LOCATION;
    }
}