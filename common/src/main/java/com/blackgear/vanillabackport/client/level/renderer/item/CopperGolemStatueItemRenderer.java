package com.blackgear.vanillabackport.client.level.renderer.item;

import com.blackgear.platform.client.v2.render.BuiltinItemRendererRegistry;
import com.blackgear.vanillabackport.common.level.block.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.block_entity.CopperGolemStatueBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;

@Environment(EnvType.CLIENT)
public class CopperGolemStatueItemRenderer implements BuiltinItemRendererRegistry.Renderer {
    private final CopperGolemStatueBlockEntity statue;
    
    public CopperGolemStatueItemRenderer(CopperGolemStatueBlock statue) {
        this.statue = new CopperGolemStatueBlockEntity(BlockPos.ZERO, statue.defaultBlockState());
    }
    
    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        BlockEntityRenderer<CopperGolemStatueBlockEntity> renderer = dispatcher.getRenderer(this.statue);
        
        if (renderer != null) {
            if (context == ItemDisplayContext.GROUND) {
                pose.translate(0.0F, 0.5F, 1.0F);
            } else {
                pose.translate(0.0F, 1.5F, 1.0F);
            }
            
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.scale(-1.0F, -1.0F, 1.0F);

            BlockItemStateProperties properties = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            CopperGolemStatueBlock.Pose statuePose = properties.get(CopperGolemStatueBlock.POSE);
            this.statue.setBlockState(
                this.statue.getBlockState().setValue(
                    CopperGolemStatueBlock.POSE,
                    statuePose != null ? statuePose : CopperGolemStatueBlock.Pose.STANDING
                )
            );

            renderer.render(this.statue, 0f, pose, buffer, light, overlay);
        }
    }
}