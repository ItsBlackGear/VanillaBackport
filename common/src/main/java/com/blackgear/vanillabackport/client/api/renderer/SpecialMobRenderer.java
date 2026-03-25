package com.blackgear.vanillabackport.client.api.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SpecialMobRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    public static <R> Optional<Supplier<R>> create(
        EntityRendererProvider.Context context,
        Function<EntityRendererProvider.Context, R> factory,
        RenderConditions conditions
    ) {
        return Optional.of(new Supplier<>() {
            private R instance;

            @Override
            public R get() {
                if (!conditions.apply()) return null;
                if (this.instance == null) this.instance = factory.apply(context);
                return this.instance;
            }
        });
    }

    public static <R> Optional<Supplier<R>> create(EntityRendererProvider.Context context, Function<EntityRendererProvider.Context, R> factory) {
        return create(context, factory, RenderConditions.DEFAULT);
    }

    public abstract Optional<ResourceLocation> getTexture(T entity);

    public abstract Optional<M> getModel(T entity);
}