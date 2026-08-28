package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.SimpleEquipmentLayer;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.NautilusModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.items.NautilusArmorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class NautilusRenderer<T extends AbstractNautilus> extends AgeableMobRenderer<T, NautilusModel<T>> {
    private static final ResourceLocation NAUTILUS_LOCATION = new ResourceLocation("textures/entity/nautilus/nautilus.png");
    private static final ResourceLocation NAUTILUS_BABY_LOCATION = new ResourceLocation("textures/entity/nautilus/nautilus_baby.png");
    static final ResourceLocation NAUTILUS_SADDLE_LOCATION = new ResourceLocation("textures/entity/nautilus/saddle/saddle.png");
    
    public NautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS)), new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_BABY)), 0.7F);
        this.addLayer(new SimpleEquipmentLayer<>(
            this,
            entity -> entity.getArmor().getItem() instanceof NautilusArmorItem armor
                ? Optional.ofNullable(armor.getTexture())
                : Optional.empty(),
            entity -> true,
            new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_ARMOR)),
            null
        ));
        this.addLayer(SimpleEquipmentLayer.of(
            this,
            NAUTILUS_SADDLE_LOCATION,
            AbstractNautilus::isSaddled,
            new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_SADDLE)),
            null
        ));
    }
    
    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.isBaby() ? NAUTILUS_BABY_LOCATION : NAUTILUS_LOCATION;
    }
}