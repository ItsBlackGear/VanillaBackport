package com.blackgear.vanillabackport.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for lazy initialization of entity models.
 * This ensures models are only baked when first accessed during rendering,
 * preventing timing issues with model layer registration.
 *
 * @param <T> The entity type
 * @param <M> The model type
 */
@Environment(EnvType.CLIENT)
public class LazyModel<T extends Entity, M extends EntityModel<T>> implements Supplier<M> {
    private M model;
    private final Supplier<M> factory;

    private LazyModel(Supplier<M> factory) {
        this.factory = factory;
    }

    /**
     * Creates a lazy model that will be baked on first access.
     *
     * @param models The entity model set
     * @param layer The model layer location
     * @param factory A function that creates the model from the baked model part
     * @param <T> The entity type
     * @param <M> The model type
     * @return A lazy model supplier
     */
    public static <T extends Entity, M extends EntityModel<T>> LazyModel<T, M> of(
        EntityModelSet models,
        ModelLayerLocation layer,
        Function<ModelPart, M> factory
    ) {
        return new LazyModel<>(() -> factory.apply(models.bakeLayer(layer)));
    }

    /**
     * Creates a lazy model with a custom factory function.
     *
     * @param factory A supplier that creates the model
     * @param <T> The entity type
     * @param <M> The model type
     * @return A lazy model supplier
     */
    public static <T extends Entity, M extends EntityModel<T>> LazyModel<T, M> of(Supplier<M> factory) {
        return new LazyModel<>(factory);
    }

    @Override
    public M get() {
        if (this.model == null) {
            this.model = this.factory.get();
        }
        return this.model;
    }
}

