package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public class ClientAsset {
    public static final Codec<ClientAsset> CODEC = ResourceLocation.CODEC.xmap(ClientAsset::new, ClientAsset::id);
    public static final MapCodec<ClientAsset> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientAsset> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, ClientAsset::id,
        ResourceLocation.STREAM_CODEC, ClientAsset::path,
        ClientAsset::new
    );
    private final ResourceLocation id;
    private final ResourceLocation path;
    
    public ClientAsset(ResourceLocation id, ResourceLocation path) {
        this.id = id;
        this.path = path;
    }
    
    public ResourceLocation id() {
        return id;
    }
    
    public ResourceLocation path() {
        return path;
    }
    
    public ClientAsset(ResourceLocation path) {
        this(path, path.withPath(string -> "textures/" + string + ".png"));
    }
}