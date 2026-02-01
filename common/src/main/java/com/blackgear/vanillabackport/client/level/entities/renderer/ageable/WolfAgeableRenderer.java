package com.blackgear.vanillabackport.client.level.entities.renderer.ageable;

import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.client.level.entities.model.wolf.BabyWolfModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.client.util.LazyModel;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entities.wolf.WolfVariants;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class WolfAgeableRenderer extends AbstractAgeableRenderer<Wolf, WolfModel<Wolf>> {
    public WolfAgeableRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected AdultAndBabyModelPair<WolfModel<Wolf>> bakeModels(EntityRendererProvider.Context context) {
        return new AdultAndBabyModelPair<>(null, LazyModel.of(context.getModelSet(), ModModelLayers.WOLF_BABY, BabyWolfModel::new).get());
    }

    @Override
    protected ResourceLocation getAdultTexture(Wolf entity) {
        Optional<WolfVariant> variant = VariantDataHolder.<WolfVariant>getHolder(entity).getVariantData();
        if (variant.isPresent()) {
            if (!VariantUtils.matches(ModBuiltinRegistries.WOLF_VARIANTS, variant.get(), WolfVariants.PALE)) {
                if (entity.isTame()) {
                    return variant.get().assetInfo().tame().path();
                } else {
                    return entity.isAngry()
                        ? variant.get().assetInfo().angry().path()
                        : variant.get().assetInfo().wild().path();
                }
            }
        }

        return null;
    }

    @Override
    protected ResourceLocation getBabyTexture(Wolf entity) {
//        Optional<WolfVariant> variant = VariantDataHolder.<WolfVariant>getHolder(entity).getVariantData();
//        if (variant.isPresent() && variant.get().babyAssetsInfo() != null) {
//            if (entity.isTame()) {
//                return variant.get().babyAssetsInfo().tame().path();
//            } else {
//                return entity.isAngry()
//                    ? variant.get().babyAssetsInfo().angry().path()
//                    : variant.get().babyAssetsInfo().wild().path();
//            }
//        }

        return this.getAdultTexture(entity);
    }
}