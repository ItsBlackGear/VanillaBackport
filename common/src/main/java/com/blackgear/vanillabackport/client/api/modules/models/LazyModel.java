package com.blackgear.vanillabackport.client.api.modules.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class LazyModel<T extends Entity, M extends EntityModel<T>> implements Supplier<M> {
    private M model;
    private final Supplier<M> factory;

    private LazyModel(Supplier<M> factory) {
        this.factory = factory;
    }

    public static <T extends Entity, M extends EntityModel<T>> LazyModel<T, M> of(
        EntityModelSet models,
        ModelLayerLocation layer,
        Function<ModelPart, M> factory
    ) {
        return new LazyModel<>(() -> factory.apply(models.bakeLayer(layer)));
    }

    public static <T extends Entity, M extends EntityModel<T>> LazyModel<T, M> of(Supplier<M> factory) {
        return new LazyModel<>(factory);
    }

    @Override
    public M get() {
        if (this.model == null) this.model = this.factory.get();
        return this.model;
    }
}

