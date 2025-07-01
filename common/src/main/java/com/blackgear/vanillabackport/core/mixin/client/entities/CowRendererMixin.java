package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariant;
import com.blackgear.vanillabackport.common.level.entities.AnimalVariantHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumMap;
import java.util.Map;

@Mixin(CowRenderer.class)
public abstract class CowRendererMixin extends MobRendererMixin<Cow, CowModel<Cow>> {
    @Unique private static final Map<AnimalVariant, ResourceLocation> TEXTURE_BY_VARIANT = Util.make(Maps.newEnumMap(AnimalVariant.class), map -> {
        map.put(AnimalVariant.COLD, VanillaBackport.resource("textures/entity/cow/cold_cow.png"));
        map.put(AnimalVariant.WARM, VanillaBackport.resource("textures/entity/cow/warm_cow.png"));
    });

    @Unique private Map<AnimalVariant, CowModel<Cow>> modelByVariant;

    public CowRendererMixin(EntityRendererProvider.Context context, CowModel<Cow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.modelByVariant = this.bakeModels(context);
    }

    @Unique
    private Map<AnimalVariant, CowModel<Cow>> bakeModels(EntityRendererProvider.Context context) {
        EnumMap<AnimalVariant, CowModel<Cow>> map = new EnumMap<>(AnimalVariant.class);
        map.put(AnimalVariant.DEFAULT, this.defaultModel);
        map.put(AnimalVariant.WARM, new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)));
        map.put(AnimalVariant.COLD, new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)));

        return map;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cow;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Cow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        if (variant != null && variant != AnimalVariant.DEFAULT) {
            cir.setReturnValue(TEXTURE_BY_VARIANT.get(variant));
        }
    }

    @Override
    public void render(Cow entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        AnimalVariant variant = AnimalVariantHolder.testFor(entity).getVariant();
        this.model = this.modelByVariant.get(variant);

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}