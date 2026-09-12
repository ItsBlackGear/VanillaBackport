package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.frog.FrogDataVariant;
import com.blackgear.vanillabackport.core.compat.ClientCompat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.FrogModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Frog;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class FrogVariantRenderer extends SpecialMobRenderer<Frog, FrogModel<Frog>> {
    @Override
    public Optional<ResourceLocation> getTexture(Frog entity) {
        if (ClientCompat.hasQuarkFrogTexture(entity)) return Optional.empty();

        return VariantDataHolder.<FrogDataVariant>getHolder(entity)
            .flatMap(VariantDataHolder::getVariantData)
            .map(variant -> variant.assetInfo().path());
    }

    @Override
    public Optional<FrogModel<Frog>> getModel(Frog entity) {
        return Optional.empty();
    }
}