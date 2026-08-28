package com.blackgear.vanillabackport.core.forge.emissive;

import com.blackgear.vanillabackport.client.api.modules.models.EmissiveQuad;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EmissiveModelWrapper implements BakedModel {
    private final BakedModel baseModel;
    private final BakedModel emissiveModel;

    public EmissiveModelWrapper(BakedModel baseModel, BakedModel emissiveModel) {
        this.baseModel = baseModel;
        this.emissiveModel = emissiveModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return getQuads(state, side, rand, ModelData.EMPTY, null);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        long seed = rand.nextLong();
        List<BakedQuad> quads = new ArrayList<>(baseModel.getQuads(state, side, RandomSource.create(seed), extraData, renderType));
        List<BakedQuad> emissiveQuads = emissiveModel.getQuads(state, side, RandomSource.create(seed), extraData, renderType);
        
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

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        return baseModel.getModelData(level, pos, state, modelData);
    }
}