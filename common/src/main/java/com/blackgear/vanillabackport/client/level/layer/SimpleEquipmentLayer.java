package com.blackgear.vanillabackport.client.level.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class SimpleEquipmentLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Function<T, Optional<ResourceLocation>> textureSupplier;
    private final Predicate<T> shouldRender;
    private final EntityModel<T> model;
    private final @Nullable EntityModel<T> babyModel;

    public SimpleEquipmentLayer(
        RenderLayerParent<T, M> renderer,
        Function<T, Optional<ResourceLocation>> textureSupplier,
        Predicate<T> shouldRender,
        EntityModel<T> model,
        @Nullable EntityModel<T> babyModel
    ) {
        super(renderer);
        this.textureSupplier = textureSupplier;
        this.shouldRender = shouldRender;
        this.model = model;
        this.babyModel = babyModel;
    }

    public static <T extends LivingEntity, M extends EntityModel<T>> SimpleEquipmentLayer<T, M> of(
        RenderLayerParent<T, M> renderer,
        Map<ItemLike, ResourceLocation> textureByItem,
        EquipmentSlot slot,
        Predicate<T> shouldRender,
        EntityModel<T> model,
        @Nullable EntityModel<T> babyModel
    ) {
        return new SimpleEquipmentLayer<>(
            renderer,
            entity -> Optional.ofNullable(textureByItem.get(entity.getItemBySlot(slot).getItem())),
            shouldRender,
            model,
            babyModel
        );
    }
    
    public static <T extends LivingEntity, M extends EntityModel<T>> SimpleEquipmentLayer<T, M> of(
        RenderLayerParent<T, M> renderer,
        ResourceLocation texture,
        Predicate<T> shouldRender,
        EntityModel<T> model,
        @Nullable EntityModel<T> babyModel
    ) {
        return new SimpleEquipmentLayer<>(renderer, entity -> Optional.of(texture), shouldRender, model, babyModel);
    }

    @Override
    public void render(
        PoseStack pose,
        MultiBufferSource buffer,
        int packedLight,
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        float ageInTicks,
        float netHeadYaw,
        float headPitch
    ) {
        if (!this.shouldRender.test(entity)) return;
        if (entity.isBaby() && this.babyModel == null) return;
        
        Optional<ResourceLocation> texture = this.textureSupplier.apply(entity);
        if (texture.isEmpty()) return;
        
        EntityModel<T> model = entity.isBaby() ? this.babyModel : this.model;
        this.getParentModel().copyPropertiesTo(model);
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(texture.get()));
        model.renderToBuffer(pose, vertices, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}