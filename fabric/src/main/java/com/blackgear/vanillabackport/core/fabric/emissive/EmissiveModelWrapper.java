package com.blackgear.vanillabackport.core.fabric.emissive;

import com.blackgear.vanillabackport.client.api.modules.emissive_models.EmissiveQuad;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class EmissiveModelWrapper extends ForwardingBakedModel {
    private static final int FULL_BRIGHT_LIGHTMAP = 0x00F000F0;

    private final BakedModel emissiveModel;

    public EmissiveModelWrapper(BakedModel baseModel, BakedModel emissiveModel) {
        this.wrapped = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);

        context.pushTransform(EmissiveModelWrapper::forceFullBright);
        emissiveModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        super.emitItemQuads(stack, randomSupplier, context);

        context.pushTransform(EmissiveModelWrapper::forceFullBright);
        emissiveModel.emitItemQuads(stack, randomSupplier, context);
        context.popTransform();
    }

    private static boolean forceFullBright(MutableQuadView quad) {
        quad.lightmap(0, FULL_BRIGHT_LIGHTMAP);
        quad.lightmap(1, FULL_BRIGHT_LIGHTMAP);
        quad.lightmap(2, FULL_BRIGHT_LIGHTMAP);
        quad.lightmap(3, FULL_BRIGHT_LIGHTMAP);
        return true;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        List<BakedQuad> quads = new ArrayList<>(wrapped.getQuads(state, face, random));

        List<BakedQuad> emissiveQuads = emissiveModel.getQuads(state, face, random);
        for (BakedQuad quad : emissiveQuads) {
            quads.add(new EmissiveQuad(quad));
        }

        return quads;
    }
}