package com.blackgear.vanillabackport.client.level.entities.variant;

import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariantHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class AbstractVariantRenderer<T extends LivingEntity, M extends EntityModel<T>> {
    protected final Map<AnimalVariant, M> modelByVariant;

    protected AbstractVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    public abstract Map<AnimalVariant, M> bakeModels(EntityRendererProvider.Context context);

    public abstract Map<AnimalVariant, ResourceLocation> textureByVariant();

    public ResourceLocation getTexture(T entity) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        return this.textureByVariant().get(variant);
    }

    public M getModel(T entity) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        return this.modelByVariant.get(variant);
    }
}