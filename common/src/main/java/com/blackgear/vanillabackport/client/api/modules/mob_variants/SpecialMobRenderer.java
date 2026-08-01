package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class SpecialMobRenderer <T extends LivingEntity, M extends EntityModel<T>> {
    public static <T extends LivingEntity, M extends EntityModel<T>, R extends SpecialMobRenderer<T, M>> SpecialMobRenderer<T, M> create(
        EntityRendererProvider.Context context,
        Function<EntityRendererProvider.Context, R> factory,
        RenderConditions conditions
    ) {
        return new SpecialMobRenderer<>() {
            private R instance;
            private boolean initialized = false;
            
            private R get() {
                if (!this.initialized) {
                    if (conditions.apply()) {
                        this.instance = factory.apply(context);
                    }
                    
                    this.initialized = true;
                }
                
                return this.instance;
            }
            
            @Override
            public Optional<ResourceLocation> getTexture(T entity) {
                R renderer = this.get();
                return renderer != null ? renderer.getTexture(entity) : Optional.empty();
            }
            
            @Override
            public Optional<M> getModel(T entity) {
                R renderer = this.get();
                return renderer != null ? renderer.getModel(entity) : Optional.empty();
            }
            
            @Override
            public void ifPresent(Consumer<SpecialMobRenderer<T, M>> consumer) {
                R renderer = this.get();
                if (renderer != null) {
                    consumer.accept(renderer);
                }
            }
        };
    }
    
    public static <T extends LivingEntity, M extends EntityModel<T>, R extends SpecialMobRenderer<T, M>> SpecialMobRenderer<T, M> create(
        EntityRendererProvider.Context context,
        Function<EntityRendererProvider.Context, R> factory
    ) {
        return create(context, factory, RenderConditions.DEFAULT);
    }
    
    public static <L> void addLayer(RenderConditions condition, Supplier<L> factory, Consumer<L> action) {
        if (condition.apply()) action.accept(factory.get());
    }
    
    public static <R extends LivingEntityRenderer<?, ?>, L> void append(LivingEntityRenderer<?, ?> renderer, Class<R> clazz, RenderConditions condition, Function<R, L> factory, Consumer<L> appender) {
        if (clazz.isInstance(renderer) && condition.apply()) {
            R type = clazz.cast(renderer);
            appender.accept(factory.apply(type));
        }
    }
    
    public static <R extends LivingEntityRenderer<?, ?>, L> void append(LivingEntityRenderer<?, ?> renderer, Class<R> clazz, Function<R, L> factory, Consumer<L> appender) {
        append(renderer, clazz, RenderConditions.DEFAULT, factory, appender);
    }
    
    public abstract Optional<ResourceLocation> getTexture(T entity);
    
    public abstract Optional<M> getModel(T entity);
    
    public void ifPresent(Consumer<SpecialMobRenderer<T, M>> consumer) { /* NO-OP */ }
}