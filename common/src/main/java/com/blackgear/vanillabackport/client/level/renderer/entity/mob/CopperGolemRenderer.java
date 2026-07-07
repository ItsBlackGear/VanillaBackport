package com.blackgear.vanillabackport.client.level.renderer.entity.mob;

import com.blackgear.vanillabackport.client.level.layer.BlockDecorationLayer;
import com.blackgear.vanillabackport.client.level.layer.LivingEntityEmissiveLayer;
import com.blackgear.vanillabackport.client.level.model.entity.CopperGolemModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolemOxidationLevels;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemModel<CopperGolem>> {
    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(ModModelLayers.COPPER_GOLEM)), 0.5F);
        this.addLayer(new LivingEntityEmissiveLayer<>(
            this,
            getEyeTextureLocationProvider(),
            (golem, ageInTicks) -> 1.0F,
            new CopperGolemModel<>(context.bakeLayer(ModModelLayers.COPPER_GOLEM)),
            RenderType::eyes,
            false
        ));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new BlockDecorationLayer<>(
            this,
            context.getBlockRenderDispatcher(),
            golem -> {
                ItemStack antennaItem = golem.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA);
                if (antennaItem.getItem() instanceof BlockItem block) {
                    return block.getBlock().defaultBlockState();
                } else {
                    return null;
                }
            },
            this.model::applyBlockOnAntennaTransform));
    }

    private Function<CopperGolem, ResourceLocation> getEyeTextureLocationProvider() {
        return golem -> CopperGolemOxidationLevels.getOxidationLevel(golem.getWeatherState()).eyeTexture();
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolem entity) {
        return CopperGolemOxidationLevels.getOxidationLevel(entity.getWeatherState()).texture();
    }

    @Override
    protected void scale(CopperGolem livingEntity, PoseStack poseStack, float partialTickTime) {
        super.scale(livingEntity, poseStack, partialTickTime);
        poseStack.translate(0.0, 1.5, 0.0);
    }
}