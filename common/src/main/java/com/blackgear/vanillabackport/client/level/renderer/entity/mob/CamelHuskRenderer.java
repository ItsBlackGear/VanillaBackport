package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import net.minecraft.client.renderer.entity.CamelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.camel.Camel;

public class CamelHuskRenderer extends CamelRenderer {
    public static final ResourceLocation CAMEL_HUSK_LOCATION = new ResourceLocation("textures/entity/camel/camel_husk.png");
    
    public CamelHuskRenderer(EntityRendererProvider.Context context) {
        super(context, ModModelLayers.CAMEL_HUSK);
    }
    
    @Override
    public ResourceLocation getTextureLocation(Camel entity) {
        return CAMEL_HUSK_LOCATION;
    }
}