package com.blackgear.vanillabackport.client.level.item;

import com.blackgear.platform.client.v2.render.DynamicItemRenderer;
import com.blackgear.platform.core.util.event.ResultHolder;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpawnEggRenderer implements DynamicItemRenderer.Renderer {
    public static final Set<ItemLike> SPAWN_EGGS = Set.of(
        Items.ALLAY_SPAWN_EGG,
        ModItems.ARMADILLO_SPAWN_EGG.get(),
        Items.AXOLOTL_SPAWN_EGG,
        Items.BAT_SPAWN_EGG,
        Items.BEE_SPAWN_EGG,
        Items.BLAZE_SPAWN_EGG,
        Items.CAMEL_SPAWN_EGG,
        Items.CAT_SPAWN_EGG,
        Items.CAVE_SPIDER_SPAWN_EGG,
        Items.CHICKEN_SPAWN_EGG,
        Items.COD_SPAWN_EGG,
        Items.COW_SPAWN_EGG,
        ModItems.CREAKING_SPAWN_EGG.get(),
        Items.CREEPER_SPAWN_EGG,
        Items.DOLPHIN_SPAWN_EGG,
        Items.DONKEY_SPAWN_EGG,
        Items.DROWNED_SPAWN_EGG,
        Items.ELDER_GUARDIAN_SPAWN_EGG,
        Items.ENDERMAN_SPAWN_EGG,
        Items.ENDERMITE_SPAWN_EGG,
        Items.EVOKER_SPAWN_EGG,
        Items.FOX_SPAWN_EGG,
        Items.FROG_SPAWN_EGG,
        Items.GHAST_SPAWN_EGG,
        Items.GLOW_SQUID_SPAWN_EGG,
        Items.GOAT_SPAWN_EGG,
        Items.GUARDIAN_SPAWN_EGG,
        ModItems.HAPPY_GHAST_SPAWN_EGG.get(),
        Items.HOGLIN_SPAWN_EGG,
        Items.HORSE_SPAWN_EGG,
        Items.HUSK_SPAWN_EGG,
        Items.IRON_GOLEM_SPAWN_EGG,
        Items.LLAMA_SPAWN_EGG,
        Items.MAGMA_CUBE_SPAWN_EGG,
        Items.MOOSHROOM_SPAWN_EGG,
        Items.MULE_SPAWN_EGG,
        Items.OCELOT_SPAWN_EGG,
        Items.PANDA_SPAWN_EGG,
        Items.PARROT_SPAWN_EGG,
        Items.PHANTOM_SPAWN_EGG,
        Items.PIG_SPAWN_EGG,
        Items.PIGLIN_SPAWN_EGG,
        Items.PIGLIN_BRUTE_SPAWN_EGG,
        Items.PILLAGER_SPAWN_EGG,
        Items.POLAR_BEAR_SPAWN_EGG,
        Items.PUFFERFISH_SPAWN_EGG,
        Items.RABBIT_SPAWN_EGG,
        Items.RAVAGER_SPAWN_EGG,
        Items.SALMON_SPAWN_EGG,
        Items.SHEEP_SPAWN_EGG,
        Items.SHULKER_SPAWN_EGG,
        Items.SILVERFISH_SPAWN_EGG,
        Items.SKELETON_SPAWN_EGG,
        Items.SKELETON_HORSE_SPAWN_EGG,
        Items.SLIME_SPAWN_EGG,
        Items.SNIFFER_SPAWN_EGG,
        Items.SNOW_GOLEM_SPAWN_EGG,
        Items.SPIDER_SPAWN_EGG,
        Items.SQUID_SPAWN_EGG,
        Items.STRAY_SPAWN_EGG,
        Items.STRIDER_SPAWN_EGG,
        Items.TADPOLE_SPAWN_EGG,
        Items.TRADER_LLAMA_SPAWN_EGG,
        Items.TROPICAL_FISH_SPAWN_EGG,
        Items.TURTLE_SPAWN_EGG,
        Items.VEX_SPAWN_EGG,
        Items.VILLAGER_SPAWN_EGG,
        Items.VINDICATOR_SPAWN_EGG,
        Items.WANDERING_TRADER_SPAWN_EGG,
        Items.WARDEN_SPAWN_EGG,
        Items.WITCH_SPAWN_EGG,
        Items.WITHER_SKELETON_SPAWN_EGG,
        Items.WOLF_SPAWN_EGG,
        Items.ZOGLIN_SPAWN_EGG,
        Items.ZOMBIE_SPAWN_EGG,
        Items.ZOMBIE_HORSE_SPAWN_EGG,
        Items.ZOMBIE_VILLAGER_SPAWN_EGG,
        Items.ZOMBIFIED_PIGLIN_SPAWN_EGG
    );

    private static final Map<ItemLike, ModelResourceLocation> EGG_MODELS = buildModels();

    private static Map<ItemLike, ModelResourceLocation> buildModels() {
        Map<ItemLike, ModelResourceLocation> models = new HashMap<>();
        for (ItemLike item : SPAWN_EGGS) models.put(item, create(item.asItem()));
        return models;
    }

    private static ModelResourceLocation create(Item item) {
        return new ModelResourceLocation(VanillaBackport.resource(BuiltInRegistries.ITEM.getKey(item).getPath()), "inventory");
    }

    @Override
    public boolean shouldUse() {
        return !VanillaBackport.CLIENT_CONFIG.useLegacySpawnEggs.get();
    }

    @Override
    public void renderFirstPerson(
        ItemStack stack,
        ItemDisplayContext context,
        boolean leftHand,
        PoseStack pose,
        MultiBufferSource buffer,
        int light,
        int overlay,
        BakedModel model,
        ItemModelShaper shaper,
        ItemColors colors
    ) {
        model = shaper.getModelManager().getModel(EGG_MODELS.get(stack.getItem()));
        model.getTransforms().getTransform(context).apply(leftHand, pose);
        pose.translate(-0.5F, -0.5F, -0.5F);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, true);
        VertexConsumer vertices = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil());
        this.renderModelLists(model, stack, light, overlay, pose, vertices, colors);
    }

    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.submit(shaper.getModelManager().getModel(EGG_MODELS.get(stack.getItem())));
    }

    @Override
    public Set<ModelResourceLocation> registerModels() {
        Set<ModelResourceLocation> models = ImmutableSet.of();
        models = ImmutableSet.<ModelResourceLocation>builder()
            .addAll(models)
            .addAll(EGG_MODELS.values())
            .build();
        return models;
    }

    @Override
    public void renderQuadList(PoseStack pose, VertexConsumer buffer, List<BakedQuad> quads, ItemStack stack, int light, int overlay, ItemColors colors) {
        PoseStack.Pose last = pose.last();

        for (BakedQuad quad : quads) {
            int tint = -1;
            float red = (float) (tint >> 16 & 0xFF) / 255.0F;
            float green = (float) (tint >> 8 & 0xFF) / 255.0F;
            float blue = (float) (tint & 0xFF) / 255.0F;
            buffer.putBulkData(last, quad, red, green, blue, light, overlay);
        }
    }
}