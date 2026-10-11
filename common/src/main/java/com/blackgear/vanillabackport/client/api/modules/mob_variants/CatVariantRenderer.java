package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.cat.CatDataVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CatVariantRenderer extends SpecialMobRenderer<Cat, CatModel<Cat>> {
    @Override
    public Optional<ResourceLocation> getTexture(Cat entity) {
        return VariantDataHolder.<CatDataVariant>getHolder(entity)
            .flatMap(VariantDataHolder::getVariantData)
            .map(variant -> variant.assetInfo().path());
    }

    @Override
    public Optional<CatModel<Cat>> getModel(Cat entity) {
        return Optional.empty();
    }
}