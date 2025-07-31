package com.blackgear.vanillabackport.common.api.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
}