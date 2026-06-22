package com.blackgear.vanillabackport.client.api.modules.mob_variants;


import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.core.ModChecker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractVariantRenderer<T extends LivingEntity, M extends EntityModel<T>, V, E extends Enum<E>> extends SpecialMobRenderer<T, M> {
    protected final Map<E, M> modelByVariant;

    public AbstractVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    protected abstract Map<E, M> bakeModels(EntityRendererProvider.Context context);

    protected Optional<V> getVariant(T entity) {
        return VariantDataHolder.<V>getHolder(entity).getVariantData();
    }

    protected abstract E getModelType(V variant);

    protected abstract ResourceLocation getTexture(V variant);

    protected abstract BuiltInCoreRegistry<V> getRegistry();

    protected abstract ResourceKey<V> getDefaultVariant();

    private boolean isDefaultVariant(V variant) {
        return VariantUtils.matches(this.getRegistry(), variant, this.getDefaultVariant());
    }

    @Override
    public Optional<ResourceLocation> getTexture(T entity) {
        Optional<V> variant = this.getVariant(entity);
        return variant.filter(v -> !this.isDefaultVariant(v)).map(this::getTexture);
    }

    @Override
    public Optional<M> getModel(T entity) {
        if (ModChecker.MIXED_LITTER_LOADED) return Optional.empty();

        Optional<V> variant = this.getVariant(entity);
        if (variant.isEmpty()) return Optional.empty();

        E modelType = this.getModelType(variant.get());
        M model = this.modelByVariant.get(modelType);

        return Optional.ofNullable(model);
    }
}