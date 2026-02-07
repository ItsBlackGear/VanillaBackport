package com.blackgear.vanillabackport.core.emissive;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps an existing BakedModel and adds emissive quads on top
 */
public class EmissiveModelWrapper implements BakedModel {
    private final BakedModel baseModel;
    private final BakedModel emissiveModel;

    public EmissiveModelWrapper(BakedModel baseModel, BakedModel emissiveModel) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        List<BakedQuad> quads = new ArrayList<>();

        // Add base model quads (normal rendering)
        quads.addAll(baseModel.getQuads(state, side, rand));

        // Add emissive quads with full brightness
        List<BakedQuad> emissiveQuads = emissiveModel.getQuads(state, side, rand);
        for (BakedQuad quad : emissiveQuads) {
            quads.add(new EmissiveQuad(quad));
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return baseModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return baseModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return baseModel.isCustomRenderer();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return baseModel.getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return baseModel.getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return baseModel.getOverrides();
    }
}
