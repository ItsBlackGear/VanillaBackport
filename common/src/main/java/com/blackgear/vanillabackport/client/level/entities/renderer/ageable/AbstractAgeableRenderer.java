package com.blackgear.vanillabackport.client.level.entities.renderer.ageable;

import com.blackgear.vanillabackport.client.level.entities.model.AdultAndBabyModelPair;
import com.blackgear.vanillabackport.core.ModConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class AbstractAgeableRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    protected final AdultAndBabyModelPair<M> models;

    public AbstractAgeableRenderer(EntityRendererProvider.Context context) {
        this.models = this.bakeModels(context);
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

    protected abstract AdultAndBabyModelPair<M> bakeModels(EntityRendererProvider.Context context);

    protected abstract ResourceLocation getAdultTexture(T entity);

    protected abstract ResourceLocation getBabyTexture(T entity);

    public Optional<ResourceLocation> getTexture(T entity) {
        return ModConstants.USE_LEGACY_BABY_MODELS || !entity.isBaby()
            ? Optional.ofNullable(this.getAdultTexture(entity))
            : Optional.ofNullable(this.getBabyTexture(entity));
    }

    public Optional<M> getModel(T entity) {
        return Optional.ofNullable(this.models.getModel(entity.isBaby() && !ModConstants.USE_LEGACY_BABY_MODELS));
    }
}

