package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class WolfVariantRenderer extends SpecialMobRenderer<Wolf, WolfModel<Wolf>> {
    @Override
    public Optional<ResourceLocation> getTexture(Wolf entity) {
        return VariantDataHolder.<WolfVariant>getHolder(entity)
            .flatMap(VariantDataHolder::getVariantData)
            .map(variant -> {
                var assets = variant.assetInfo();
                var texture = entity.isTame() ? assets.tame() : (entity.isAngry() ? assets.angry() : assets.wild());
                return texture.path();
            });
    }
    
    @Override
    public Optional<WolfModel<Wolf>> getModel(Wolf entity) {
        return Optional.empty();
    }
}