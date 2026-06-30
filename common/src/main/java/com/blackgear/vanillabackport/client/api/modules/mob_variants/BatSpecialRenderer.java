package com.blackgear.vanillabackport.client.api.modules.mob_variants;

import com.blackgear.vanillabackport.client.level.entity.model.BatModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

@Environment(EnvType.CLIENT) @SuppressWarnings("unchecked")
public class BatSpecialRenderer<T extends LivingEntity, M extends EntityModel<T>> extends SpecialMobRenderer<T, M> {
    private static final ResourceLocation TEXTURE = VanillaBackport.resource("textures/entity/bat.png");
    private final M model;

    public BatSpecialRenderer(EntityRendererProvider.Context context) {
        this.model = (M) new BatModel(context.bakeLayer(ModModelLayers.BAT));
    }

    @Override
    public Optional<ResourceLocation> getTexture(T entity) {
        return Optional.of(TEXTURE);
    }

    @Override
    public Optional<M> getModel(T entity) {
        return Optional.ofNullable(this.model);
    }
}
