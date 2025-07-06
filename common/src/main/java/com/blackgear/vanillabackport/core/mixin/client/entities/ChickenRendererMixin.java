package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariantHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRendererMixin<Chicken, ChickenModel<Chicken>> {
    @Unique private static final Map<AnimalVariant, ResourceLocation> TEXTURE_BY_VARIANT = Util.make(Maps.newEnumMap(AnimalVariant.class), map -> {
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/chicken/cold_chicken.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/chicken/warm_chicken.png"));
    });

    @Unique private Map<AnimalVariant, ChickenModel<Chicken>> modelByVariant;

    public ChickenRendererMixin(EntityRendererProvider.Context context, ChickenModel<Chicken> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.modelByVariant = this.bakeModels(context);
    }

    @Unique
    private Map<AnimalVariant, ChickenModel<Chicken>> bakeModels(EntityRendererProvider.Context context) {
        Map<AnimalVariant, ChickenModel<Chicken>> map = Maps.newEnumMap(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, this.defaultModel);
        map.put(AnimalVariant.WARM, this.defaultModel);
        map.put(AnimalVariant.COLD, this.defaultModel);
//        map.put(AnimalVariant.COLD, new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)));

        return map;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Chicken;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Chicken entity, CallbackInfoReturnable<ResourceLocation> cir) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        if (variant != null && variant != AnimalVariant.DEFAULT) {
            cir.setReturnValue(TEXTURE_BY_VARIANT.get(variant));
        }
    }

    @Override
    public void render(Chicken entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        this.model = this.modelByVariant.get(variant);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}