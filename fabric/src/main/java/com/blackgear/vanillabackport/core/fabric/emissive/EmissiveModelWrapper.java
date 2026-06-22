package com.blackgear.vanillabackport.core.fabric.emissive;

import com.blackgear.vanillabackport.client.api.modules.emissive_models.EmissiveQuad;
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

public class EmissiveModelWrapper extends ForwardingBakedModel {
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

        context.pushTransform(quad -> {
            quad.lightmap(0, 0x00F000F0);
            quad.lightmap(1, 0x00F000F0);
            quad.lightmap(2, 0x00F000F0);
            quad.lightmap(3, 0x00F000F0);
            return true;
        });

        if (emissiveModel instanceof ForwardingBakedModel) {
            emissiveModel.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else {
            Direction.stream().forEach(d -> context.fallbackConsumer().accept(emissiveModel));
            context.fallbackConsumer().accept(emissiveModel);
        }

        context.popTransform();
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        super.emitItemQuads(stack, randomSupplier, context);

        context.pushTransform(quad -> {
            quad.lightmap(0, 0x00F000F0);
            quad.lightmap(1, 0x00F000F0);
            quad.lightmap(2, 0x00F000F0);
            quad.lightmap(3, 0x00F000F0);
            return true;
        });

        if (emissiveModel instanceof ForwardingBakedModel) {
            emissiveModel.emitItemQuads(stack, randomSupplier, context);
        } else {
            context.fallbackConsumer().accept(emissiveModel);
        }

        context.popTransform();
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