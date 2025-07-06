package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariantHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.Map;

@Mixin(PigRenderer.class)
public abstract class PigRendererMixin extends MobRendererMixin<Pig, PigModel<Pig>> {
    @Unique private static final Map<AnimalVariant, ResourceLocation> TEXTURE_BY_VARIANT = Util.make(Maps.newEnumMap(AnimalVariant.class), map -> {
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/pig/cold_pig.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/pig/warm_pig.png"));
    });

    @Unique private Map<AnimalVariant, PigModel<Pig>> modelByVariant;

    public PigRendererMixin(EntityRendererProvider.Context context, PigModel<Pig> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.modelByVariant = this.bakeModels(context);
    }

    @Unique
    private Map<AnimalVariant, PigModel<Pig>> bakeModels(EntityRendererProvider.Context context) {
        EnumMap<AnimalVariant, PigModel<Pig>> map = new EnumMap<>(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, this.defaultModel);
        map.put(AnimalVariant.WARM, this.defaultModel);
        map.put(AnimalVariant.COLD, this.defaultModel);
//        map.put(AnimalVariant.COLD, new ColdPigModel<>(context.bakeLayer(ModModelLayers.COLD_PIG)));

        return map;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Pig;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Pig entity, CallbackInfoReturnable<ResourceLocation> cir) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        if (variant != null && variant != AnimalVariant.DEFAULT) {
            cir.setReturnValue(TEXTURE_BY_VARIANT.get(variant));
        }
    }

    @Override
    public void render(Pig entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        this.model = this.modelByVariant.get(variant);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}