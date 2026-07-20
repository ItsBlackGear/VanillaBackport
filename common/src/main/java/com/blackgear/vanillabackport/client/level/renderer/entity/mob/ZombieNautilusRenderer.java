package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.ZombieNautilusVariantRenderer;
import com.blackgear.vanillabackport.client.level.layer.SimpleEquipmentLayer;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.NautilusModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.ZombieNautilus;
import com.blackgear.vanillabackport.common.level.item.NautilusArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ZombieNautilusRenderer extends MobRenderer<ZombieNautilus, NautilusModel<ZombieNautilus>> {
    private static final ResourceLocation NAUTILUS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/nautilus/zombie_nautilus.png");
    private final SpecialMobRenderer<ZombieNautilus, NautilusModel<ZombieNautilus>> renderer;
    private final NautilusModel<ZombieNautilus> fallback;
    
    public ZombieNautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new NautilusModel<>(context.bakeLayer(ModModelLayers.ZOMBIE_NAUTILUS)), 0.7F);
        this.renderer = SpecialMobRenderer.create(context, ZombieNautilusVariantRenderer::new);
        this.fallback = new NautilusModel<>(context.bakeLayer(ModModelLayers.ZOMBIE_NAUTILUS));
        this.addLayer(new SimpleEquipmentLayer<>(
            this,
            entity -> entity.getBodyArmorItem().getItem() instanceof NautilusArmorItem armor
                ? Optional.ofNullable(armor.getTexture())
                : Optional.empty(),
            entity -> true,
            new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_ARMOR)),
            null
        ));
        this.addLayer(SimpleEquipmentLayer.of(
            this,
            NautilusRenderer.NAUTILUS_SADDLE_LOCATION,
            AbstractNautilus::isSaddled,
            new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS_SADDLE)),
            null
        ));
    }
    
    @Override
    public ResourceLocation getTextureLocation(ZombieNautilus entity) {
        return this.renderer.getTexture(entity).orElse(NAUTILUS_LOCATION);
    }
    
    @Override
    public void render(ZombieNautilus entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer.getModel(entity).orElse(this.fallback);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}