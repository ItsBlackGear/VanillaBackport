package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.ClientAsset;

/**
 * added for retro-compatibility with MC Earth Mobs Addon, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true, since = "1.3")
public class ModelAndTexture<T> extends com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture<T> {
    public ModelAndTexture(T model, ClientAsset asset) {
        super(model, asset);
    }
}
