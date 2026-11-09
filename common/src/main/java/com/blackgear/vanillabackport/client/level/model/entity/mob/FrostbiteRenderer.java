package com.blackgear.vanillabackport.client.level.model.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.FrostbiteOuterLayer;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.zombie.Frostbite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class FrostbiteRenderer extends AbstractZombieRenderer<Frostbite, ZombieModel<Frostbite>> {
    private static final ResourceLocation FROSTBITE_LOCATION = new ResourceLocation("textures/entity/zombie/frostbite.png");
    
    public FrostbiteRenderer(EntityRendererProvider.Context context) {
        super(
            context,
            new ZombieModel<>(context.bakeLayer(ModModelLayers.FROSTBITE)),
            new ZombieModel<>(context.bakeLayer(ModModelLayers.FROSTBITE_INNER_ARMOR)),
            new ZombieModel<>(context.bakeLayer(ModModelLayers.FROSTBITE_OUTER_ARMOR))
        );
        this.addLayer(new FrostbiteOuterLayer(this, context.getModelSet()));
    }
    
    @Override
    public ResourceLocation getTextureLocation(Frostbite entity) {
        return FROSTBITE_LOCATION;
    }
}