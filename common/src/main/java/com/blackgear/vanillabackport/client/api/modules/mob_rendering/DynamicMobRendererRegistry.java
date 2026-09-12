package com.blackgear.vanillabackport.client.api.modules.mob_rendering;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT) @SuppressWarnings({"unchecked", "rawtypes"})
public class DynamicMobRendererRegistry {
    private static final Map<Class<?>, Function<EntityRendererProvider.Context, SpecialMobRenderer<?, ?>>> REGISTRY = new HashMap<>();
    
    static {
        register(CowRenderer.class, context -> SpecialMobRenderer.create(context, CowVariantRenderer::new, RenderConditions.FARM_ANIMALS));
        register(PigRenderer.class, context -> SpecialMobRenderer.create(context, PigVariantRenderer::new, RenderConditions.FARM_ANIMALS));
        register(ChickenRenderer.class, context -> SpecialMobRenderer.create(context, ChickenVariantRenderer::new, RenderConditions.FARM_ANIMALS));
        register(FrogRenderer.class, context -> new FrogVariantRenderer());
        register(CatRenderer.class, context -> new CatVariantRenderer());
        register(WolfRenderer.class, context -> new WolfVariantRenderer());
        register(BatRenderer.class, context -> SpecialMobRenderer.create(context, BatSpecialRenderer::new, RenderConditions.BATS));
    }
    
    public static <R extends LivingEntityRenderer<T, M>, T extends LivingEntity, M extends EntityModel<T>> void register(Class<R> renderer, Function<EntityRendererProvider.Context, SpecialMobRenderer<T, M>> factory) {
        REGISTRY.put(renderer, (Function) factory);
    }
    
    public static <T extends LivingEntity, M extends EntityModel<T>> SpecialMobRenderer<T, M> getRenderer(LivingEntityRenderer<T, M> renderer, EntityRendererProvider.Context context) {
        Function<EntityRendererProvider.Context, SpecialMobRenderer<?, ?>> factory = REGISTRY.get(renderer.getClass());
        return factory != null ? (SpecialMobRenderer<T, M>) factory.apply(context) : null;
    }
}