package com.blackgear.vanillabackport.client.level.entities;

import com.blackgear.vanillabackport.client.level.entities.model.BatModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class BatRendererModule<T extends Entity, M extends EntityModel<T>> {
    private final M model;

    public BatRendererModule(EntityRendererProvider.Context context) {
        this.model = (M) new BatModel(context.bakeLayer(ModModelLayers.BAT));
    }

    public ResourceLocation getTexture() {
        return VanillaBackport.resource("textures/entity/bat.png");
    }

    public boolean enabled() {
        return VanillaBackport.CLIENT_CONFIG.updatedBatModel.get();
    }

    public Optional<M> getModel() {
        return this.enabled() ? Optional.ofNullable(this.model) : Optional.empty();
    }
}