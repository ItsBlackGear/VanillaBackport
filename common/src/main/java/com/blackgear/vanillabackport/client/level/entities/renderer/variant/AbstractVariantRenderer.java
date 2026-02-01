package com.blackgear.vanillabackport.client.level.entities.renderer.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.common.api.variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.variant.VariantUtils;
import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.ModConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class AbstractVariantRenderer<T extends LivingEntity, M extends EntityModel<T>, V, E extends Enum<E>> {
    protected final Map<E, AdultAndBabyModelPair<M>> modelByVariant;

    public AbstractVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    public static <R> Supplier<R> create(
        EntityRendererProvider.Context context,
        Function<EntityRendererProvider.Context, R> factory
    ) {
        return new Supplier<>() {
            private R instance;

            @Override
            public R get() {
                if (this.instance == null) this.instance = factory.apply(context);
                return this.instance;
            }
        };
    }

    protected abstract Map<E, AdultAndBabyModelPair<M>> bakeModels(EntityRendererProvider.Context context);

    protected Optional<V> getVariant(T entity) {
        return VariantDataHolder.<V>getHolder(entity).getVariantData();
    }

    protected abstract E getModelType(V variant);

    protected abstract ResourceLocation getAdultTexture(V variant);

    protected abstract ResourceLocation getBabyTexture(V variant);

    protected abstract BuiltInCoreRegistry<V> getRegistry();

    protected abstract ResourceKey<V> getDefaultVariant();

    private boolean shouldUseBabyTexture(T entity) {
        return entity.isBaby() && !ModConstants.USE_LEGACY_BABY_MODELS;
    }

    private boolean isDefaultVariant(V variant) {
        return VariantUtils.matches(this.getRegistry(), variant, this.getDefaultVariant());
    }

    public Optional<ResourceLocation> getTexture(T entity) {
        Optional<V> variant = this.getVariant(entity);
//        if (variant.isEmpty()) return Optional.empty();
//
//        if (this.shouldUseBabyTexture(entity)) {
//            return Optional.ofNullable(this.getBabyTexture(variant.get()));
//        }
//
//        return this.isDefaultVariant(variant.get()) ? Optional.empty() : Optional.ofNullable(this.getAdultTexture(variant.get()));

        return variant.filter(v -> !this.isDefaultVariant(v)).map(this::getAdultTexture);
    }

    public Optional<M> getModel(T entity) {
        if (ModChecker.MIXED_LITTER_LOADED.get()) return Optional.empty();

        Optional<V> variant = this.getVariant(entity);
        if (variant.isEmpty()) return Optional.empty();

        E modelType = this.getModelType(variant.get());
        AdultAndBabyModelPair<M> modelPair = this.modelByVariant.get(modelType);
//        M model = modelPair.getModel(this.shouldUseBabyTexture(entity));
        M model = modelPair.getModel(false);

        return Optional.ofNullable(model);
    }
}
