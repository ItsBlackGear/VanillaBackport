package com.blackgear.vanillabackport.client.level.entities.renderer.ageable;

import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.client.level.entities.model.cow.BabyCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;

@Environment(EnvType.CLIENT)
public class MushroomCowAgeableRenderer extends AbstractAgeableRenderer<MushroomCow, CowModel<MushroomCow>> {
    private static final ResourceLocation RED_TEXTURE = new ResourceLocation("textures/entity/cow/mooshroom_red_baby.png");
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation("textures/entity/cow/mooshroom_brown_baby.png");

    public MushroomCowAgeableRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected AdultAndBabyModelPair<CowModel<MushroomCow>> bakeModels(EntityRendererProvider.Context context) {
        return new AdultAndBabyModelPair<>(null, new BabyCowModel<>(context.bakeLayer(ModModelLayers.MOOSHROOM_BABY)));
    }

    @Override
    protected ResourceLocation getAdultTexture(MushroomCow entity) {
        return null;
    }

    @Override
    protected ResourceLocation getBabyTexture(MushroomCow entity) {
        return switch (entity.getVariant()) {
            case RED -> RED_TEXTURE;
            case BROWN -> BROWN_TEXTURE;
        };
    }
}