package com.blackgear.vanillabackport.client.level.entities.renderer.ageable;

import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.client.level.entities.model.BabySheepModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

@Environment(EnvType.CLIENT)
public class SheepAgeableRenderer extends AbstractAgeableRenderer<Sheep, SheepModel<Sheep>> {
    private static final ResourceLocation SHEEP_BABY_TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_baby.png");

    public SheepAgeableRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected AdultAndBabyModelPair<SheepModel<Sheep>> bakeModels(EntityRendererProvider.Context context) {
//        return null;
        return new AdultAndBabyModelPair<>(null, new BabySheepModel<>(context.bakeLayer(ModModelLayers.SHEEP_BABY)));
    }

    @Override
    protected ResourceLocation getAdultTexture(Sheep entity) {
        return null;
    }

    @Override
    protected ResourceLocation getBabyTexture(Sheep entity) {
        return SHEEP_BABY_TEXTURE;
    }
}