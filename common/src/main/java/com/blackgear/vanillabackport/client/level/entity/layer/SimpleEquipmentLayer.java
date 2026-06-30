package com.blackgear.vanillabackport.client.level.entity.layer;

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
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class SimpleEquipmentLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final Map<ItemStack, ResourceLocation> textureByItem;
    private final BiPredicate<ItemStack, T> itemGetter;
    private final Predicate<T> shouldRender;
    private final EntityModel<T> model;
    private final EntityModel<T> babyModel;

    public SimpleEquipmentLayer(
        RenderLayerParent<T, M> renderer,
        Map<ItemStack, ResourceLocation> textureByItem,
        BiPredicate<ItemStack, T> itemGetter,
        Predicate<T> shouldRender,
        EntityModel<T> model,
        EntityModel<T> babyModel
    ) {
        super(renderer);
        this.textureByItem = textureByItem;
        this.itemGetter = itemGetter;
        this.shouldRender = shouldRender;
        this.model = model;
        this.babyModel = babyModel;
    }

    public SimpleEquipmentLayer(
        RenderLayerParent<T, M> renderer,
        Map<ItemStack, ResourceLocation> textureByItem,
        EquipmentSlot slot,
        Predicate<T> shouldRender,
        EntityModel<T> model,
        EntityModel<T> babyModel
    ) {
        this(renderer, textureByItem, (stack, entity) -> entity.getItemBySlot(slot).is(stack.getItem()), shouldRender, model, babyModel);
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
        Optional<ResourceLocation> texture = this.textureByItem.entrySet().stream()
            .filter(entry -> this.itemGetter.test(entry.getKey(), entity))
            .map(Map.Entry::getValue)
            .findFirst();
        if (this.shouldRender.test(entity) && texture.isPresent() && (!entity.isBaby() || this.babyModel != null)) {
            EntityModel<T> model = entity.isBaby() ? this.babyModel : this.model;
            this.getParentModel().copyPropertiesTo(model);
            model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(texture.get()));
            model.renderToBuffer(pose, vertices, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}