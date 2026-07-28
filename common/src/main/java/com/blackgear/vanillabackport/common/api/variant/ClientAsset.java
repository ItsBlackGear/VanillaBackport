package com.blackgear.vanillabackport.common.api.variant;

import net.minecraft.resources.ResourceLocation;

/**
 * added for retro-compatibility with MC Earth Mobs Addon, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true, since = "1.3")
public class ClientAsset extends com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset {
    public ClientAsset(ResourceLocation id, ResourceLocation path) {
        super(id, path);
    }
}
