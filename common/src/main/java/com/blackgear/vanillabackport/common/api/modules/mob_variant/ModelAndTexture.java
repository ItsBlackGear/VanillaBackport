package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ModelAndTexture<T>(T model, ClientAsset asset) {

    public ModelAndTexture(T model, ResourceLocation path) {
        this(model, new ClientAsset(path));
    }

    public static <T> MapCodec<ModelAndTexture<T>> codec(Codec<T> codec, T entry) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec.optionalFieldOf("model", entry).forGetter(ModelAndTexture::model),
            ClientAsset.DEFAULT_FIELD_CODEC.forGetter(ModelAndTexture::asset)
        ).apply(instance, ModelAndTexture::new));
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(StreamCodec<RegistryFriendlyByteBuf, T> modelCodec) {
        return StreamCodec.composite(
            modelCodec, ModelAndTexture::model,
            ClientAsset.STREAM_CODEC, ModelAndTexture::asset,
            ModelAndTexture::new
        );
    }
}