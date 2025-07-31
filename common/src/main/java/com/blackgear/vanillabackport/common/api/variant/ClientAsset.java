package com.blackgear.vanillabackport.common.api.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public record ClientAsset(ResourceLocation id, ResourceLocation path) {
    public static final Codec<ClientAsset> CODEC = ResourceLocation.CODEC.xmap(ClientAsset::new, ClientAsset::id);
    public static final MapCodec<ClientAsset> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");

    public ClientAsset(ResourceLocation path) {
        this(path, path.withPath(string -> "textures/" + string + ".png"));
    }
}